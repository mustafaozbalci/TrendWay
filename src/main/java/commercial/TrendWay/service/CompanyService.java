package commercial.TrendWay.service;

import commercial.TrendWay.dto.CompanyDTO;
import commercial.TrendWay.entity.Company;
import commercial.TrendWay.exceptions.BadRequestException;
import commercial.TrendWay.exceptions.ErrorCodes;
import commercial.TrendWay.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private static final Logger logger = LoggerFactory.getLogger(CompanyService.class);

    private final CompanyRepository companyRepository;

    /**
     * Registers a new company.
     *
     * @param companyDTO DTO containing company details.
     * @return The registered Company entity.
     */
    public Company registerCompany(CompanyDTO companyDTO) {
        logger.info("Registering company: {}", companyDTO.getName());

        Optional<Company> existingCompany = companyRepository.findByName(companyDTO.getName());
        if (existingCompany.isPresent()) {
            logger.warn("Company already exists: {}", companyDTO.getName());
            throw new BadRequestException("Company already exists", ErrorCodes.COMPANY_ALREADY_EXISTS);
        }

        Company company = new Company();
        company.setName(companyDTO.getName());
        company.setAddress(companyDTO.getAddress());
        company.setEmail(companyDTO.getEmail());
        company.setPhoneNumber(companyDTO.getPhoneNumber());
        company.setWalletId(companyDTO.getWalletId());

        companyRepository.save(company);

        logger.info("Company registered: {}", companyDTO.getName());
        return company;
    }
}

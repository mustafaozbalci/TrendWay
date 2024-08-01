package commercial.TrendWay.service;

import commercial.TrendWay.dto.ResponseModel;
import commercial.TrendWay.entity.Company;
import commercial.TrendWay.exceptions.BadRequestException;
import commercial.TrendWay.exceptions.ErrorCodes;
import commercial.TrendWay.repository.CompanyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
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
     * @param company Company entity.
     * @return ResponseEntity with ResponseModel indicating the result of the operation.
     */
    @Transactional
    public ResponseEntity<ResponseModel> registerCompany(Company company) {
        logger.info("Registering company: {}", company.getName());
        Optional<Company> existingCompany = companyRepository.findByName(company.getName());
        if (existingCompany.isPresent()) {
            throw new BadRequestException("Company already exists", ErrorCodes.COMPANY_ALREADY_EXISTS);
        }

        Company savedCompany = companyRepository.save(company);
        logger.info("Company registered: {}", company.getName());
        return ResponseEntity.status(201).body(new ResponseModel(201, "Company registered successfully", savedCompany));
    }
}

package commercial.TrendWay.service;

import commercial.TrendWay.dto.CompanyDTO;
import commercial.TrendWay.dto.ResponseModel;
import commercial.TrendWay.entity.Company;
import commercial.TrendWay.entity.User;
import commercial.TrendWay.exceptions.BadRequestException;
import commercial.TrendWay.exceptions.ErrorCodes;
import commercial.TrendWay.repository.CompanyRepository;
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
    private final UserService userService;

    /**
     * Registers a new company.
     *
     * @param adminUsername The username of the admin.
     * @param adminPassword The password of the admin.
     * @param companyDTO    The company data transfer object containing company details.
     * @return ResponseEntity with the result of the operation.
     */
    public ResponseEntity<ResponseModel> registerCompany(String adminUsername, String adminPassword, CompanyDTO companyDTO) {
        logger.info("Registering company: {}", companyDTO.getName());

        User admin = userService.validateAdminUser(adminUsername, adminPassword);

        if (admin.getCompany() != null) {
            logger.warn("Admin already has a company: {}", adminUsername);
            throw new BadRequestException("Admin already has a company", ErrorCodes.USER_ALREADY_HAS_COMPANY);
        }

        Optional<Company> existingCompany = companyRepository.findByName(companyDTO.getName());
        if (existingCompany.isPresent()) {
            logger.warn("Company already exists: {}", companyDTO.getName());
            throw new BadRequestException("Company already exists", ErrorCodes.COMPANY_ALREADY_EXISTS);
        }

        Company company = new Company(companyDTO.getName(), companyDTO.getAddress(), companyDTO.getEmail(), companyDTO.getPhoneNumber(), companyDTO.getWalletId(), admin);
        companyRepository.save(company);

        admin.setCompany(company);
        userService.updateUser(admin);

        logger.info("Company registered: {}", companyDTO.getName());
        return ResponseEntity.status(201).body(new ResponseModel(201, "Company registered successfully", company));
    }

    /**
     * Retrieves a company by its ID.
     *
     * @param companyId The ID of the company.
     * @return The company entity.
     * @throws BadRequestException if the company is not found.
     */
    public Company getCompanyById(Long companyId) {
        return companyRepository.findById(companyId).orElseThrow(() -> new BadRequestException("Company not found", ErrorCodes.COMPANY_NOT_FOUND));
    }
}

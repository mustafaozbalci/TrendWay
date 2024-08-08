package commercial.TrendWay.service;

import commercial.TrendWay.dto.CompanyDTO;
import commercial.TrendWay.dto.ResponseModel;
import commercial.TrendWay.entity.Company;
import commercial.TrendWay.entity.RoleType;
import commercial.TrendWay.entity.User;
import commercial.TrendWay.exceptions.BadRequestException;
import commercial.TrendWay.exceptions.ErrorCodes;
import commercial.TrendWay.repository.CompanyRepository;
import commercial.TrendWay.repository.UserRepository;
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
    private final UserRepository userRepository;

    /**
     * Registers a new company for an admin user.
     *
     * @param adminUsername The username of the admin.
     * @param adminPassword The password of the admin.
     * @param companyDTO    The details of the company to register.
     * @return ResponseEntity with the registration status and company details.
     */
    public ResponseEntity<ResponseModel> registerCompany(String adminUsername, String adminPassword, CompanyDTO companyDTO) {
        logger.info("Registering company: {}", companyDTO.getName());

        // Find admin user by username
        User admin = userRepository.findByUsername(adminUsername).orElseThrow(() -> {
            logger.warn("Admin user not found: {}", adminUsername);
            return new BadRequestException("Admin user not found", ErrorCodes.USER_NOT_FOUND);
        });

        // Validate admin credentials and role
        if (!admin.getPassword().equals(adminPassword) || !admin.getRole().equals(RoleType.ADMIN)) {
            logger.warn("Invalid credentials or not an admin user: {}", adminUsername);
            throw new BadRequestException("Invalid credentials or not an admin user", ErrorCodes.INVALID_CREDENTIALS);
        }

        // Check if admin already has a company
        if (admin.getCompany() != null) {
            logger.warn("Admin already has a company: {}", adminUsername);
            throw new BadRequestException("Admin already has a company", ErrorCodes.USER_ALREADY_HAS_COMPANY);
        }

        // Check if company with the same name already exists
        Optional<Company> existingCompany = companyRepository.findByName(companyDTO.getName());
        if (existingCompany.isPresent()) {
            logger.warn("Company already exists: {}", companyDTO.getName());
            throw new BadRequestException("Company already exists", ErrorCodes.COMPANY_ALREADY_EXISTS);
        }

        // Create new company and set its properties
        Company company = new Company();
        company.setName(companyDTO.getName());
        company.setAddress(companyDTO.getAddress());
        company.setEmail(companyDTO.getEmail());
        company.setPhoneNumber(companyDTO.getPhoneNumber());
        company.setWalletId(companyDTO.getWalletId());
        company.setUser(admin);

        // Save company to repository
        companyRepository.save(company);

        // Update admin with the new company
        admin.setCompany(company);
        userRepository.save(admin);

        logger.info("Company registered: {}", companyDTO.getName());
        return ResponseEntity.status(201).body(new ResponseModel(201, "Company registered successfully", company));
    }
}

package commercial.TrendWay.controller;

import commercial.TrendWay.dto.CompanyDTO;
import commercial.TrendWay.dto.ResponseModel;
import commercial.TrendWay.service.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/companies")
public class CompanyController {
    private static final Logger logger = LoggerFactory.getLogger(CompanyController.class);
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseModel> registerCompany(@RequestHeader("adminUsername") String adminUsername, @RequestHeader("adminPassword") String adminPassword, @RequestBody CompanyDTO companyDTO) {
        logger.info("Request to register company: {}", companyDTO.getName());
        return companyService.registerCompany(adminUsername, adminPassword, companyDTO);
    }
}

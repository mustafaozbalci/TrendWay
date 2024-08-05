package commercial.TrendWay.controller;

import commercial.TrendWay.dto.CompanyDTO;
import commercial.TrendWay.dto.ResponseModel;
import commercial.TrendWay.entity.Company;
import commercial.TrendWay.service.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/companies")
public class CompanyController {
    private static final Logger logger = LoggerFactory.getLogger(CompanyController.class);
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseModel> registerCompany(@RequestBody CompanyDTO companyDTO) {
        logger.info("Request to register company: {}", companyDTO.getName());
        Company company = companyService.registerCompany(companyDTO);
        return ResponseEntity.status(201).body(new ResponseModel(201, "Company registered successfully", company));
    }
}

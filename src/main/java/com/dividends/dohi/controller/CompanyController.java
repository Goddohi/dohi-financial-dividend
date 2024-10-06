package com.dividends.dohi.controller;

import com.dividends.dohi.model.Company;
import com.dividends.dohi.persist.entity.CompanyEntity;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import com.dividends.dohi.service.CompanyService;

import java.util.List;

@RestController
@RequestMapping("/company") //공통경로
@AllArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    /**
     * 배당금 검색할때 자동완성
     */
    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocompleteCompany(@RequestParam String keyword) {
        return null;
    }

    /**
     * 회사 리스트 조회
     */
    @GetMapping
    public ResponseEntity<?> searchCompany(Pageable pageable) {
        Page<CompanyEntity> companyEntities = this.companyService.getAllCompany(pageable);
        return ResponseEntity.ok(companyEntities);
    }

    /**
     * 회사 및 배당금 저장
     * @param request
     * @return
     */
    @PostMapping
    public ResponseEntity<?> addCompany(@RequestBody Company request){
        String ticker = request.getTicker().trim();
        if(ObjectUtils.isEmpty(ticker)){
            throw new RuntimeException("ticker is empty");
        }
        Company company = this.companyService.save(ticker);
        return ResponseEntity.ok(company);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCompany(){
        return null;
    }

}

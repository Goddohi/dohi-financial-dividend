package com.dividends.dohi.controller;

import com.dividends.dohi.model.Company;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import com.dividends.dohi.service.CompanyService;

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
    public ResponseEntity<?> searchCompany() {
        return null;
    }
    /**
     * 배당금 데이터 저장
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

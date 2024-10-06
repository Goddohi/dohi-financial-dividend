package com.dividends.dohi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company") //공통경로
public class CompanyController {

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
    public ResponseEntity<?> addCompany(){
        return null;
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCompany(){
        return null;
    }

}

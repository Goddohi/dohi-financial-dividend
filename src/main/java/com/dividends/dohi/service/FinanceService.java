package com.dividends.dohi.service;

import com.dividends.dohi.model.Company;
import com.dividends.dohi.model.Dividend;
import com.dividends.dohi.model.ScrapedResult;
import com.dividends.dohi.persist.entity.CompanyEntity;
import com.dividends.dohi.persist.entity.DividendEntity;
import com.dividends.dohi.persist.repository.CompanyRepository;
import com.dividends.dohi.persist.repository.DividendRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FinanceService {
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    public ScrapedResult getDividendByCompanyName(String companyName) {
        // 1. 회사명을 기준으로 회사 정보를 조회
        CompanyEntity companyEntity = this.companyRepository.findByName(companyName)
                .orElseThrow(()-> new RuntimeException("Company not found"));
        // 2. 조회된 회사 ID 로 배당금 정보 조회
        List<DividendEntity> dividendEntities = this.dividendRepository.findAllByCompanyId(companyEntity.getId());
        // 3. 결과 조합 후 반환
        List<Dividend> dividends = new ArrayList<>();
        dividends = dividendEntities.stream().map(e-> Dividend.builder()
                                                    .date(e.getDate())
                                                    .dividend(e.getDividend())
                                                    .build()).collect(Collectors.toList());
        return  new ScrapedResult(Company.builder()
                .name(companyEntity.getName())
                .ticker(companyEntity.getTicker()).build(),dividends);
    }
}
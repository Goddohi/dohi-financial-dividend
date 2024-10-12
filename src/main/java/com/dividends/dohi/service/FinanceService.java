package com.dividends.dohi.service;

import com.dividends.dohi.exception.impl.NoCompanyException;
import com.dividends.dohi.model.Company;
import com.dividends.dohi.model.Dividend;
import com.dividends.dohi.model.ScrapedResult;
import com.dividends.dohi.model.constants.CacheKey;
import com.dividends.dohi.persist.entity.CompanyEntity;
import com.dividends.dohi.persist.entity.DividendEntity;
import com.dividends.dohi.persist.repository.CompanyRepository;
import com.dividends.dohi.persist.repository.DividendRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class FinanceService {
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;


    // 요청이 자주 들어오는가..   자주 변경되는 데이터인가 .. (적다) 캐씽 고려
    @Cacheable(key = "#companyName", value = CacheKey.KEY_FINANCE)
    public ScrapedResult getDividendByCompanyName(String companyName) {
        log.info("Seacrch company" + companyName);


        // 1. 회사명을 기준으로 회사 정보를 조회
        CompanyEntity companyEntity = this.companyRepository.findByName(companyName)
                .orElseThrow(()-> new NoCompanyException());
        // 2. 조회된 회사 ID 로 배당금 정보 조회
        List<DividendEntity> dividendEntities = this.dividendRepository.findAllByCompanyId(companyEntity.getId());
        // 3. 결과 조합 후 반환
        List<Dividend> dividends = new ArrayList<>();
        dividends = dividendEntities.stream().map(e-> new Dividend(e.getDate(),e.getDividend())).collect(Collectors.toList());
        return  new ScrapedResult(new Company(companyEntity.getTicker(),companyEntity.getName()),dividends);
    }
}
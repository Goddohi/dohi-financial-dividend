package com.dividends.dohi.service;

import com.dividends.dohi.model.Company;
import com.dividends.dohi.model.ScrapedResult;
import com.dividends.dohi.persist.entity.CompanyEntity;
import com.dividends.dohi.persist.entity.DividendEntity;
import com.dividends.dohi.persist.repository.CompanyRepository;
import com.dividends.dohi.persist.repository.DividendRepository;
import com.dividends.dohi.scraper.Scraper;
import lombok.AllArgsConstructor;

import org.apache.commons.collections4.Trie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompanyService {

    private final Trie trie;
    private final Scraper yahooFinanceScraper;

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    // storeCompanyAndDividend사용
    public Company save(String ticker){
        boolean exists = companyRepository.existsByTicker(ticker);
        if(exists){
            throw new RuntimeException("Company already exists ->" + ticker);
        }
        return  this.storeCompanyAndDividend(ticker);
    }


    public Page<CompanyEntity> getAllCompany(Pageable pageable){
        return this.companyRepository.findAll(pageable);
    }

    //save에서 사용
    private Company storeCompanyAndDividend(String ticker){
        //ticker를 기준으로 회사를 스크래핑
        Company company = this.yahooFinanceScraper.scrapCompanyByTicker(ticker);

        if(ObjectUtils.isEmpty(company)){
            throw  new RuntimeException("Company not found" + ticker);
        }
        // 해당 회사가 존재할 경우에는 회사의 배당금 정보를 스크래핑
        ScrapedResult scrapedResult = this.yahooFinanceScraper.scrap(company);

        // 스크래핑 결과
        CompanyEntity companyEntity = this.companyRepository.save(new CompanyEntity(company));
        //map은 컬렉션의 엘리먼트들을 다른값에 매핑해야할떄 좋은듯
        List<DividendEntity> dividendEntities = scrapedResult.getDividendEntities().stream()
                .map(e->new DividendEntity(companyEntity.getId(),e))
                .collect(Collectors.toList());

        this.dividendRepository.saveAll(dividendEntities);

        return company;
    }


    public void addAutocompleteKeyword(String keyword) {
        this.trie.put(keyword, null);
    }

    public List<String> autocomplete(String keyword) {
        return (List<String>) this.trie.prefixMap(keyword).keySet()
                .stream()
                // .limit(10) //갯수제한
                .collect(Collectors.toList());
    }

    public void deleteAutocompleteKeyword(String keyword) {
        this.trie.remove(keyword);
    }
//autocomplte과 동일한 로직 (sql의 like)
    public List<String> getCompanyNameByKeyword(String keyword) {
        Pageable limit = PageRequest.of(0,10); //리미트와 같은 느낌
        Page<CompanyEntity> companyEntities =  this.companyRepository.findByNameStartingWithIgnoreCase(keyword,limit);
        return companyEntities.stream()
                .map(e->e.getName())
                .collect(Collectors.toList());
    }

    public String deleteCompany(String ticker){
        var company = this.companyRepository.findByTicker(ticker)
                .orElseThrow(()->new RuntimeException("존재하지 않는 회사입니다."));

        this.dividendRepository.deleteAllByCompanyId(company.getId());
        this.companyRepository.delete(company);

        this.deleteAutocompleteKeyword(company.getName());
        return company.getName();
    }



}

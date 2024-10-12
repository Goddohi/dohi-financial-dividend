package com.dividends.dohi.scheduler;

import com.dividends.dohi.model.Company;
import com.dividends.dohi.model.ScrapedResult;
import com.dividends.dohi.model.constants.CacheKey;
import com.dividends.dohi.persist.entity.CompanyEntity;
import com.dividends.dohi.persist.entity.DividendEntity;
import com.dividends.dohi.persist.repository.CompanyRepository;
import com.dividends.dohi.persist.repository.DividendRepository;
import com.dividends.dohi.scraper.Scraper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j //로깅
@Component
@EnableCaching
@AllArgsConstructor
public class ScraperScheduler {
/*
    //                 초 분 시 일 월 요일
    @Scheduled(cron = "0/5 * * * * *")
    public void Test(){
        System.out.println("Test now" + System.currentTimeMillis());
    }
*/
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    private final Scraper yahooFinanceScraper;

//java파일에 cron같은경우 하드코딩이 되어있으면 변경이 필요할때 서비스 운영중에 멈췄다가 해야한다 .

    @CacheEvict(value = CacheKey.KEY_FINANCE, allEntries = true) //캐시 삭제 후 다시넣기 위해 삭제
    @Scheduled(cron = "${scheduler.scrap.yahoo}")
    public void yahooFinanaceScheduled() {
        log.info("scraping scheduler is started");
        // 저장된 회사 목록을 조회
        List<CompanyEntity> companyEntities = companyRepository.findAll();

        // 회사마다 배당금 정보를 새로 스크래핑
        for(var comapany : companyEntities) {

            log.info("scraping scheduler is " + comapany.getName());
            ScrapedResult scrapedResult = this.yahooFinanceScraper.scrap(Company.builder()
                    .name(comapany.getName())
                    .ticker(comapany.getTicker()).build());

            // 스크래핑한 배당금 정보 중 데이터베이스에 없는 값은 저장
            scrapedResult.getDividendEntities().stream()
                    // 디비든 모델을 디비든 엔티티로 매핑
                    .map(e -> new DividendEntity(comapany.getId(),e))
                    .forEach(e->{
                        boolean exists = this.dividendRepository.existsByCompanyIdAndDate(e.getCompanyId(), e.getDate());
                        // 엘리먼트를 하나씩 디비든 레파지토리에 삽입
                        if(!exists) {
                            this.dividendRepository.save(e);
                            log.info("insert new dividemd ->"+  e.toString());
                        }
                    });

            // 연속적으로 스크래핑 대상 사이트 서버에 요청을 날리지 않도록 일시정지
            try {
                Thread.sleep(3000); // 3 seconds
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }


                 }

    }
}
 

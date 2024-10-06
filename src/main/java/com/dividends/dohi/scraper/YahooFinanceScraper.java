package com.dividends.dohi.scraper;

import com.dividends.dohi.model.Company;
import com.dividends.dohi.model.Dividend;
import com.dividends.dohi.model.ScrapedResult;

import com.dividends.dohi.model.constants.Month;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class YahooFinanceScraper implements Scraper {
    //스트링 포멧사용

    private static final long START_TIME = 86400;
    private static final String STATIC_URL ="https://finance.yahoo.com/quote/%s/history/?frequency=1mo&period1=%d&period2=%d";
    private static final String SUMMARY_URL = "https://finance.yahoo.com/quote/%s?p=%s";

    public ScrapedResult scrap(Company company) {

         var scrapResult = new ScrapedResult();
         scrapResult.setCompany(company);

        try{
            long now = System.currentTimeMillis() /1000;
            String url =String.format(STATIC_URL,company.getTicker(),START_TIME,now);
            Connection connection = Jsoup.connect(url);
            Document document = connection.get();

            Elements parsingDivs = document.getElementsByAttributeValue("class","table yf-ewueuo noDl");

            Element tableEle = parsingDivs.get(0);// table 전체

            Element tbody = tableEle.children().get(1);

            List<Dividend> dividends = new ArrayList<>();
            for (Element e : tbody.children()) {
                String text = e.text();
                if(!text.endsWith("Dividend")){
                    continue;
                }
                String[] split = text.split(" ");
                int month = Month.strToNumber(split[0]);
                int day = Integer.parseInt(split[1].replace(",",""));
                int year = Integer.parseInt(split[2]);
                String dividend = split[3];

                if (month < 0) {
                    throw new RuntimeException("Unexpected Month enum value -> " + split[0]);
                }

                dividends.add(Dividend.builder()
                        .date(LocalDateTime.of(year,month,day,0,0))
                        .dividend(dividend)
                        .build());

            }
            scrapResult.setDividendEntities(dividends);
        }catch(IOException e){
            e.printStackTrace();
        }

        return scrapResult;

    }

    @Override
     public Company scrapCompanyByTicker(String ticker) {
        String url = String.format(SUMMARY_URL, ticker, ticker);

        try {
            Document document = Jsoup.connect(url).get();
           //Element titleEle = document.getElementsByTag("h1").get(1);
            Element titleEle = document.getElementsByAttributeValue("class","yf-xxbei9").get(0);
            String title = titleEle.text().split("\\(")[0].trim();

            return Company.builder()
                    .ticker(ticker)
                    .name(title).build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

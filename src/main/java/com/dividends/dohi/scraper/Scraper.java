package com.dividends.dohi.scraper;

import com.dividends.dohi.model.Company;
import com.dividends.dohi.model.ScrapedResult;

public interface Scraper {
    Company scrapCompanyByTicker(String ticker);
    ScrapedResult scrap(Company company);
}

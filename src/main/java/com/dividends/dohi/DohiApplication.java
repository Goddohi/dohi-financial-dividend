package com.dividends.dohi;

import com.dividends.dohi.model.Company;
import com.dividends.dohi.scraper.Scraper;
import com.dividends.dohi.scraper.YahooFinanceScraper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class DohiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DohiApplication.class, args);
		/*
		Scraper scraper = new YahooFinanceScraper();

		var sr = scraper.scrapCompanyByTicker("COKE");
		System.out.println(sr);
	*/
	}

}

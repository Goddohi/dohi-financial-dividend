package com.dividends.dohi;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class DohiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DohiApplication.class, args);
/* test
		try{
			String URL="https://finance.yahoo.com/quote/COKE/history/?frequency=1mo&period1=99153000&period2=1728114255";
			Connection connection = Jsoup.connect(URL);
			Document doc = connection.get();
			Elements eles= doc.getElementsByAttributeValue("class","table yf-ewueuo noDl");
			Element ele = eles.get(0);// table 전체
			//System.out.println(ele);
			Element tbody = ele.children().get(1);
			for (Element tr : tbody.children()) {
				String text = tr.text();
				if(!text.endsWith("Dividend")){
					continue;
				}
				String[] split = text.split(" ");
				String month = split[0];
				int day = Integer.parseInt(split[1].replace(",",""));
				int year = Integer.parseInt(split[2]);
				String dividend = split[3];

				System.out.println(year+"/"+month+"/"+day+"  ->  "+dividend);
			}

		}catch(IOException e){
			e.printStackTrace();
		}

 */
	}
}

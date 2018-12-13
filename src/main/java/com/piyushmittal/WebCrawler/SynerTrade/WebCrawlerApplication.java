package com.piyushmittal.WebCrawler.SynerTrade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebCrawlerApplication implements CommandLineRunner {
	@Autowired
	WebLinksAnalytics analytics;

	public static void main(String[] args) {
		SpringApplication.run(WebCrawlerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//Level defines how much depth we want to iterate for crawling
		analytics.webCrawler("https://en.wikipedia.org/wiki/Europe",1);
	}
}

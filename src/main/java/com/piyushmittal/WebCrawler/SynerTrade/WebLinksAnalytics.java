package com.piyushmittal.WebCrawler.SynerTrade;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class WebLinksAnalytics {
    private static final Logger logger = LoggerFactory.getLogger(WebLinksAnalytics.class);
    ConcurrentHashMap<String, Integer> hm = null;
    ThreadPoolExecutor executor = null;

    WebLinksAnalytics() {
        hm = new ConcurrentHashMap<String, Integer>();
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(100);
    }

    /*
      Main link crawler method. It recursively call based on the number of level we want to go.
     */
    void webCrawler(String url, int level) {

        if (level == 0)
            return;

        Matcher m = callService(url);
        while (m.find()) {

            String url1 = urlCreater(m.group(1));

            if (url1 == null)
                continue;

            if (null != hm.get(url1))
                hm.put(url1, hm.get(url1) + 1);
            else
                hm.put(url1, 1);

            final String webCrawlUrl = url1;
            Future future = executor.submit(() -> {
                webCrawler(webCrawlUrl, level - 1);

            });

            if (future.isDone()) {
                logger.info("Updating analytics data on file ..");
                printMap();
            }
        }
    }

    /*
     Utility method to create absolute path
     */
    String urlCreater(String url) {
        StringBuffer url1;
        if (url.substring(0, 5).equals("/wiki")) {
            url1 = new StringBuffer("https://en.wikipedia.org" + url);
            return url1.toString();
        } else if (url.substring(0, 4).equals("http")) {
            return url;
        } else {
            return null;
        }

    }

    /*
    This method finds all the links present in html page associated with href tag
     */
    Matcher callService(String url) {
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl = url;
        ResponseEntity<String> response = restTemplate.getForEntity(fooResourceUrl, String.class);
        Pattern p = Pattern.compile("href=\"([^\\'\\\"]+)");
        Matcher m = p.matcher(response.toString());
        return m;
    }


    void printMap() {
        //System.out.println(hm)
        String result = "";
        for (Map.Entry<String, Integer> entry : hm.entrySet()) {
            result += entry.getKey() + "," + entry.getValue() + "\n";

        }
        try {
            Files.write(Paths.get("WebCrawler.txt"), result.getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            logger.error("Error while creating Web crawler file" + e);
        }
    }


}

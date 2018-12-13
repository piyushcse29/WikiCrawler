package com.piyushmittal.WebCrawler.SynerTrade;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = WebCrawlerApplication.class)
public class WebLinksAnalyticsTest {

    @Autowired
    WebLinksAnalytics analytics;

    @Test
    public void urlCreaterWiki() {
        String url = "/wiki/Gulf_of_Tonkin";
        String match = analytics.urlCreater(url);
        assert (match.equals("https://en.wikipedia.org/wiki/Gulf_of_Tonkin"));
    }

    @Test
    public void urlCreaterHttp() {
        String url = "https://en.wikipedia.org/wiki/Gulf_of_Tonkin";
        String match = analytics.urlCreater(url);
        assert (match.equals("https://en.wikipedia.org/wiki/Gulf_of_Tonkin"));
    }

    @Test
    public void webCrawlerTest() throws InterruptedException {

        analytics.webCrawler("https://en.wikipedia.org/wiki/Europe", 1);
        Thread.sleep(1000);
        analytics.executor.shutdown();
        assert (analytics.hm.size() > 1500);
    }


}

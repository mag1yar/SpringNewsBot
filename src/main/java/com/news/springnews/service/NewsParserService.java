package com.news.springnews.service;

import com.news.springnews.model.News;
import com.news.springnews.parser.SpringNewsParser;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class NewsParserService {
    private final NewsService newsService;

    @Autowired
    public NewsParserService(NewsService newsService) {
        this.newsService = newsService;
    }

    @Scheduled(fixedDelay = 300000) // 5 minutes
    public void parseAndSaveNews() {
        List<String> newsTitles = SpringNewsParser.parseKzNewsTitles();
        for (String title : newsTitles) {
            if (!newsService.existsByTitle(title)) {
                News news = new News();
                news.setTitle(title);
                news.setContent("");

                newsService.saveNews(news);
            }
        }
    }
}

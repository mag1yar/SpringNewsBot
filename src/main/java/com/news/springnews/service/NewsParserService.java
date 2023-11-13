package com.news.springnews.service;

import com.news.springnews.bot.SpringNewsBot;
import com.news.springnews.model.News;
import com.news.springnews.parser.GameStopGameNewsParser;
import com.news.springnews.parser.KzInformburoNewsParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsParserService {
    private final NewsService newsService;

    private static KzInformburoNewsParser kzInformburoNewsParser;
    private static GameStopGameNewsParser gameStopGameNewsParser;

    private static final Logger LOG = LoggerFactory.getLogger(SpringNewsBot.class);

    @Autowired
    public NewsParserService(NewsService newsService) {
        this.newsService = newsService;

        kzInformburoNewsParser = new KzInformburoNewsParser();
        gameStopGameNewsParser = new GameStopGameNewsParser();
    }

    @Scheduled(fixedDelay = 300000) // 5 minutes
    public void parseAndSaveKzNews() {
        LOG.info("The beginning of parsing from the site Informburo.kz");
        List<News> news = kzInformburoNewsParser.parse();

        for (News item : news) {
            if (!newsService.existsByTitle(item.getTitle())) {
                newsService.saveNews(item);
            }
        }
        LOG.info("End of parsing from the site Informburo.kz");
    }

    @Scheduled(fixedDelay = 300000) // 5 minutes
    public void parseAndSaveGameNews() {
        LOG.info("The beginning of parsing from the site StopGame.ru");
        List<News> news = gameStopGameNewsParser.parse();

        for (News item : news) {
            if (!newsService.existsByTitle(item.getTitle())) {
                newsService.saveNews(item);
            }
        }
        LOG.info("End of parsing from the site StopGame.ru");
    }
}

package com.news.springnews.service;

import com.news.springnews.bot.SpringNewsBot;
import com.news.springnews.enums.SubscriptionType;
import com.news.springnews.model.News;
import com.news.springnews.parser.AnimeShikimoriNewsParser;
import com.news.springnews.parser.GameStopGameNewsParser;
import com.news.springnews.parser.INewsParser;
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
    private static AnimeShikimoriNewsParser animeShikimoriNewsParser;

    private static final Logger LOG = LoggerFactory.getLogger(SpringNewsBot.class);

    @Autowired
    public NewsParserService(NewsService newsService) {
        this.newsService = newsService;

        kzInformburoNewsParser = new KzInformburoNewsParser();
        gameStopGameNewsParser = new GameStopGameNewsParser();
        animeShikimoriNewsParser = new AnimeShikimoriNewsParser();
    }

    private void parseAndSave(SubscriptionType type) {
        INewsParser parser = null;
        switch (type) {
            case GAME_STOPGAME -> parser = gameStopGameNewsParser;
            case ANIME_SHIKIMORI -> parser = animeShikimoriNewsParser;
            case KZ_INFORMBURO -> parser = kzInformburoNewsParser;
        }

        if(parser == null) return;

        LOG.info("The beginning of parsing from the site " + type.toText());
        List<News> news = parser.parse();

        for (News item : news) {
            if (!newsService.existsByTitle(item.getTitle())) {
                newsService.saveNews(item);
            }
        }
        LOG.info("End of parsing from the site " + type.toText());
    }

    @Scheduled(fixedDelay = 300000) // 5 minutes
    public void parseAndSaveKzNews() {
        parseAndSave(SubscriptionType.KZ_INFORMBURO);
    }

    @Scheduled(fixedDelay = 300000) // 5 minutes
    public void parseAndSaveGameNews() {
        parseAndSave(SubscriptionType.GAME_STOPGAME);
    }

    @Scheduled(fixedDelay = 300000) // 5 minutes
    public void parseAndSaveAnimeNews() {
        parseAndSave(SubscriptionType.ANIME_SHIKIMORI);
    }
}

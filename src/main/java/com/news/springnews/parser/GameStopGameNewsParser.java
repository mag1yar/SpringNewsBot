package com.news.springnews.parser;

import com.news.springnews.enums.NewsType;
import com.news.springnews.model.News;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class GameStopGameNewsParser implements INewsParser {

    @Override
    public List<News> parse() {
        List<News> news = new ArrayList<>();

        try {
            var document = Jsoup.connect("https://stopgame.ru/news").get();
            var titleElements = document.select("a._title_11mk8_60");

            for (var titleElement : titleElements) {
                String title = titleElement.text();

                News newNews = new News();
                newNews.setType(NewsType.GAME_NEWS);
                newNews.setTitle(title);
                newNews.setContent("");

                news.add(newNews);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.reverse(news);

        return news;
    }
}

package com.news.springnews.parser;

import com.news.springnews.enums.SubscriptionType;
import com.news.springnews.model.News;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class AnimeShikimoriNewsParser implements INewsParser {

    @Override
    public List<News> parse() {
        List<News> newsList = new ArrayList<>();

        try {
            var document = Jsoup.connect("https://shikimori.one/forum/news").get();
            var newsElements = document.select("div.body b");

            for (var newsElement : newsElements) {
                var titleElement = newsElement.selectFirst("header a.name");
                String title = titleElement.text();

                var contentElement = newsElement.selectFirst("div.body-inner");
                String content = contentElement.text();

                News news = new News();
                news.setType(SubscriptionType.ANIME_SHIKIMORI);
                news.setTitle(title);
                news.setContent(content);

                newsList.add(news);
            }
        } catch (IOException e) {

            e.printStackTrace();
        }

        Collections.reverse(newsList);

        return newsList;
    }
}

package com.news.springnews.parser;

import com.news.springnews.enums.NewsType;
import com.news.springnews.model.News;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KzInformburoNewsParser implements INewsParser {

    @Override
    public List<News> parse() {
        List<News> news = new ArrayList<>();

        try {
            var document = Jsoup.connect("https://informburo.kz/novosti").get();
            var titleElements = document.select("li.uk-grid.uk-grid-small.uk-margin-remove-top");

            for (var titleElement : titleElements) {
                String title = titleElement.select("div.uk-width-expand > a").first().ownText();

                News newNews = new News();
                newNews.setType(NewsType.KZ_NEWS);
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

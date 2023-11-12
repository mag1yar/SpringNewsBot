package com.news.springnews.parser;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class SpringNewsParser {

    public static List<String> parseKzNewsTitles() {
        List<String> newsTitles = new ArrayList<>();

        try {
            var document = Jsoup.connect("https://informburo.kz/novosti").get();
            var titleElements = document.select("li.uk-grid.uk-grid-small.uk-margin-remove-top");

            for (var titleElement : titleElements) {
                String title = titleElement.select("div.uk-width-expand > a").first().ownText();
                newsTitles.add(title);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return newsTitles;
    }
    public static List<String> parseGameNewsTitles() {
        List<String> newsTitles = new ArrayList<>();

        try {
            var document = Jsoup.connect("https://stopgame.ru/news").get();
            var titleElements = document.select("a._title_11mk8_60");

            for (var titleElement : titleElements) {
                String title = titleElement.text();
                newsTitles.add(title);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return newsTitles;
    }
}

package com.news.springnews.parser;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SpringNewsParser {

    public SpringNewsParser() {
        super();
    }
    public String getTitle() {
        try {
            var document = Jsoup.connect("https://informburo.kz/novosti").get();
            var titleElement = document.selectFirst("li.uk-grid.uk-grid-small.uk-margin-remove-top");
            return titleElement.text();
        } catch (Exception e) {
            e.printStackTrace();
        }
    return "";
    }
}

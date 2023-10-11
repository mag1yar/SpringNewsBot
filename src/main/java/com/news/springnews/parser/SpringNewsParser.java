package com.news.springnews.parser;

import org.jsoup.Jsoup;

public class SpringNewsParser {
    public static void main(String[] args){
        try {
            var document = Jsoup.connect("https://informburo.kz/novosti").get();
            var titleElement = document.selectFirst("div.uk-width-expand");
            System.out.println(titleElement.text());
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

package com.news.springnews.parser;

import com.news.springnews.enums.SubscriptionType;
import com.news.springnews.model.News;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class GameStopGameNewsParser implements INewsParser {

    @Override
    public List<News> parse() {
        List<News> newsList = new ArrayList<>();

        try {
            var document = Jsoup.connect("https://stopgame.ru/news").get();
            var titleElements = document.select("a._title_11mk8_60");

            for (var titleElement : titleElements) {
                String title = titleElement.text();
                String newsUrl = "https://stopgame.ru" + titleElement.attr("href");

                News newNews = new News();
                newNews.setType(SubscriptionType.GAME_STOPGAME);
                newNews.setTitle(title);

                String content = fetchContent(newsUrl);
                newNews.setContent(content);

                newsList.add(newNews);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.reverse(newsList);

        return newsList;
    }

    private String fetchContent(String newsUrl) throws IOException {
        var newsDocument = Jsoup.connect(newsUrl).get();
        var contentElement = newsDocument.selectFirst("div._content_1jfi9_10");

        // Exclude elements with class "_read-more_1jfi9_1796"
        contentElement.select("div._read-more_1jfi9_1796").remove();
        contentElement.select("div._video-wrapper_1jfi9_394").remove();
        contentElement.select("div._twitter-embed_1jfi9_375").remove();

        // Convert HTML to Telegram message entity format
        String formattedContent = convertToTelegramFormat(contentElement);

        return formattedContent;
    }
}

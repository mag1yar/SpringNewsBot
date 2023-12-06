package com.news.springnews.parser;

import com.news.springnews.bot.SpringNewsBot;
import com.news.springnews.enums.SubscriptionType;
import com.news.springnews.model.News;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class KzInformburoNewsParser implements INewsParser {

    private static final Logger LOG = LoggerFactory.getLogger(SpringNewsBot.class);

    @Override
    public List<News> parse() {
        List<News> newsList = new ArrayList<>();

        try {
            var document = Jsoup.connect("https://informburo.kz/novosti").get();
            var titleElements = document.select("li.uk-grid.uk-grid-small.uk-margin-remove-top");

            for (var titleElement : titleElements) {
                String link = titleElement.select("div.uk-width-expand > a").first().attr("href");
                String title = titleElement.select("div.uk-width-expand > a").first().ownText();

                News news = new News();
                news.setType(SubscriptionType.KZ_INFORMBURO);
                news.setTitle(title);
                String content = fetchContent(link);
                news.setContent(content);

                newsList.add(news);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.reverse(newsList);

        return newsList;
    }

    private String fetchContent(String newsUrl) throws IOException {
        var newsDocument = Jsoup.connect(newsUrl).get();

        // Extract article-excerpt and article content
        var articleExcerptElement = newsDocument.selectFirst(".article-excerpt");
        var articleElement = newsDocument.selectFirst(".article");
        articleElement.select("div.read-more").remove();

        // Convert HTML to Telegram message entity format
        String formattedExcerpt = convertToTelegramFormat(articleExcerptElement);
        String formattedContent = convertToTelegramFormat(articleElement);

        // Concatenate and wrap in <p> tags
        String formattedContentWithParagraphs = formattedExcerpt + "\n\n" + formattedContent;

        return formattedContentWithParagraphs;
    }
}

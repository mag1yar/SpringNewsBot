package com.news.springnews.parser;

import com.news.springnews.model.News;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

import java.util.ArrayList;
import java.util.List;

public interface INewsParser {
    List<News> parse();

    default String convertToTelegramFormat(Element element) {
        StringBuilder result = new StringBuilder();

        for (var childNode : element.childNodes()) {
            if (childNode instanceof TextNode) {
                result.append(((TextNode) childNode).text());
            } else if (childNode instanceof Element) {
                Element childElement = (Element) childNode;

                switch (childElement.tagName()) {
                    case "b":
                    case "strong":
                        result.append("<b>").append(convertToTelegramFormat(childElement)).append("</b>");
                        break;
                    case "i":
                    case "em":
                        result.append("<i>").append(convertToTelegramFormat(childElement)).append("</i>");
                        break;
                    case "code":
                        result.append("<code>").append(convertToTelegramFormat(childElement)).append("</code>");
                        break;
                    case "s":
                    case "strike":
                    case "del":
                        result.append("<s>").append(convertToTelegramFormat(childElement)).append("</s>");
                        break;
                    case "u":
                        result.append("<u>").append(convertToTelegramFormat(childElement)).append("</u>");
                        break;
                    case "pre":
                        result.append("<pre>").append(convertToTelegramFormat(childElement)).append("</pre>");
                        break;
                    case "p":
                        result.append(convertToTelegramFormat(childElement)).append("\n\n");
                        break;
                    case "br":
                        result.append("\n");
                        break;
                    case "li":
                        result.append("- ").append(convertToTelegramFormat(childElement)).append("\n");
                        break;
                    default:
                        // If the tag is not recognized, simply process its children
                        result.append(convertToTelegramFormat(childElement));
                        break;
                }
            }
        }

        return result.toString().trim();
    }
}

package com.news.springnews.parser;

import com.news.springnews.model.News;

import java.util.ArrayList;
import java.util.List;

public interface INewsParser {
    List<News> parse();
}

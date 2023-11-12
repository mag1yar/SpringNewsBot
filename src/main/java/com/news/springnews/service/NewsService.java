package com.news.springnews.service;

import com.news.springnews.model.News;
import com.news.springnews.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewsService {
    private final NewsRepository newsRepository;

    @Autowired
    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public News saveNews(News news) {
        return newsRepository.save(news);
    }

    public boolean existsByTitle(String title) {
        return newsRepository.existsByTitle(title);
    }
}

package com.news.springnews.repository;

import com.news.springnews.enums.SubscriptionType;
import com.news.springnews.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    boolean existsByTitle(String title);
    News findByType(SubscriptionType type);
    News findFirstByIsSendAndType(boolean isSend, SubscriptionType type);
}

package com.news.springnews.repository;

import com.news.springnews.enums.SubscriptionType;
import com.news.springnews.model.User;
import com.news.springnews.model.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {
    UserSubscription findByUserAndType(User user, SubscriptionType type);
    List<UserSubscription> findAllByIsActive(boolean isActive);
    List<UserSubscription> findAllByIsActiveAndType(boolean isActive, SubscriptionType type);
}

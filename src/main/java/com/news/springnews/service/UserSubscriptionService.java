package com.news.springnews.service;

import com.news.springnews.enums.SubscriptionType;
import com.news.springnews.model.User;
import com.news.springnews.model.UserSubscription;
import com.news.springnews.repository.UserSubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserSubscriptionService {
    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;



    public void subscribeUserToNews(User user, SubscriptionType subscriptionType) {
        UserSubscription existingUserSubscription = userSubscriptionRepository.findByUserAndType(user, subscriptionType);

        if (existingUserSubscription != null) {
            existingUserSubscription.setActive(true);
            userSubscriptionRepository.save(existingUserSubscription);
        } else {
            UserSubscription userSubscription = new UserSubscription();
            userSubscription.setUser(user);
            userSubscription.setActive(true);
            userSubscription.setType(subscriptionType);
            userSubscriptionRepository.save(userSubscription);
        }
    }

    public void unsubscribeUserFromNews(User user, SubscriptionType subscriptionType) {
        UserSubscription existingUserSubscription = userSubscriptionRepository.findByUserAndType(user, subscriptionType);

        if (existingUserSubscription != null) {
            existingUserSubscription.setActive(false);
            userSubscriptionRepository.save(existingUserSubscription);
        } else {
            UserSubscription userSubscription = new UserSubscription();
            userSubscription.setUser(user);
            userSubscription.setActive(false);
            userSubscription.setType(subscriptionType);
            userSubscriptionRepository.save(userSubscription);
        }
    }
}

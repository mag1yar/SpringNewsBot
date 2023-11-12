package com.news.springnews.service;

import com.news.springnews.model.News;
import com.news.springnews.model.User;
import com.news.springnews.repository.NewsRepository;
import com.news.springnews.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getExistingOrCreateUser(Long telegramId, String telegramName) {
        User existingUser = userRepository.findByTelegramId(telegramId);

        if(existingUser != null) {
            return existingUser;
        }

        User user = new User();
        user.setTelegramId(telegramId);
        user.setTelegramName(telegramName);


        return userRepository.save(user);
    }
}

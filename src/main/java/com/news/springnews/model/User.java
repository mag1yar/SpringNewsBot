package com.news.springnews.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long telegramId;
    private String telegramName;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<UserSubscription> subscriptions = new ArrayList<>();

    public void setTelegramId(Long telegramId) {
        this.telegramId = telegramId;
    }
    public Long getTelegramId() {
        return telegramId;
    }
    public void setTelegramName(String telegramName) {
        this.telegramName = telegramName;
    }
    public String getTelegramName() {
        return telegramName;
    }
    public List<UserSubscription> getSubscriptions() {
        return subscriptions;
    }
}

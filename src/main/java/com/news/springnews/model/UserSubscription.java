package com.news.springnews.model;

import com.news.springnews.enums.SubscriptionType;
import jakarta.persistence.*;

@Entity
@Table(name = "user_subscriptions")
public class UserSubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private SubscriptionType type;

    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void setUser(User user) {
        this.user = user;
    }
    public User getUser() {
        return user;
    }
    public void setType(SubscriptionType type) {
        this.type = type;
    }
    public SubscriptionType getType() {
        return type;
    }
    public void setActive(boolean active) {
        isActive = active;
    }
    public boolean getActive() {
        return isActive;
    }
}

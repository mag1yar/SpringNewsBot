package com.news.springnews.model;

import com.news.springnews.enums.SubscriptionType;
import jakarta.persistence.*;

@Entity
@Table(name = "news")
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private SubscriptionType type;
    @Column(unique = true)
    private String title;
    @Column(columnDefinition = "text")
    private String content;
    private boolean isSend;

    public News() {
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getContent() {
        return content;
    }
    public void setType(SubscriptionType type) {
        this.type = type;
    }
    public SubscriptionType getType() {
        return type;
    }
    public void setSend(boolean send) {
        isSend = send;
    }
    public boolean getSend() {
        return isSend;
    }
}

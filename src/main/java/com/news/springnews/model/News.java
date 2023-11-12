package com.news.springnews.model;

import com.news.springnews.enums.NewsType;
import jakarta.persistence.*;

@Entity
@Table(name = "news")
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private NewsType type;
    @Column(unique = true)
    private String title;
    private String content;

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

    public void setType(NewsType type) {
        this.type = type;
    }

    public NewsType getType() {
        return type;
    }
}

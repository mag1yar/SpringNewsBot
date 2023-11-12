package com.news.springnews;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.news.springnews.repository")
@EnableScheduling
public class SpringNewsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringNewsApplication.class, args);
	}

}

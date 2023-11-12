package com.news.springnews.enums;

public enum SubscriptionType {
    KZ_INFORMBURO,
    GAME_STOPGAME;


    public String toText() {
        switch (this) {
            case KZ_INFORMBURO -> {
                return "Informburo";
            }
            case GAME_STOPGAME -> {
                return "StopGame";
            }
            default -> {
                return "Subscription Error";
            }
        }
    }
}

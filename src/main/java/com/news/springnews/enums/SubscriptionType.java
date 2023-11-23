package com.news.springnews.enums;

public enum SubscriptionType {
    KZ_INFORMBURO,
    GAME_STOPGAME,
    ANIME_SHIKIMORI;

    public String toText() {
        switch (this) {
            case KZ_INFORMBURO -> {
                return "Informburo";
            }
            case GAME_STOPGAME -> {
                return "StopGame";
            }
            case ANIME_SHIKIMORI -> {
                return "Shikimori";
            }
            default -> {
                return "Subscription Error";
            }
        }
    }
}

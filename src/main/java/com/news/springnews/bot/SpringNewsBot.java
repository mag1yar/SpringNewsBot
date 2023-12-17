package com.news.springnews.bot;

import com.news.springnews.constants.Command;
import com.news.springnews.enums.SubscriptionType;
import com.news.springnews.model.News;
import com.news.springnews.model.User;
import com.news.springnews.model.UserSubscription;
import com.news.springnews.service.NewsService;
import com.news.springnews.service.UserService;
import com.news.springnews.service.UserSubscriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
public class SpringNewsBot extends TelegramLongPollingBot {

    private static final Logger LOG = LoggerFactory.getLogger(SpringNewsBot.class);

    private final NewsService newsService;
    private final UserService userService;
    private final UserSubscriptionService userSubscriptionService;

    @Autowired
    public SpringNewsBot(@Value("${bot.token}") String token, NewsService newsService, UserService userService, UserSubscriptionService userSubscriptionService) {
        super(token);
        this.newsService = newsService;
        this.userService = userService;
        this.userSubscriptionService = userSubscriptionService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(!update.hasMessage() || !update.getMessage().hasText()) return;

        var message = update.getMessage().getText().toLowerCase();
        var chatId = update.getMessage().getChatId();
        var userName = update.getMessage().getChat().getUserName();

        User user = userService.getExistingOrCreateUser(chatId, userName);

        if (message.equals(Command.START.toLowerCase())) {
            startCommand(chatId, userName);
        } else if (message.equals(Command.Home.toLowerCase())) {
            homeCommand(user);
        } else if (message.equals(Command.SUBSCRIPTIONS.toLowerCase())) {
            subscriptionsCommand(user);
        } else if (message.equals(Command.NEWS_KZ.toLowerCase())) {
            kzNewsCommand(chatId, userName);
        } else if (message.equals(Command.NEWS_GAMES.toLowerCase())) {
            gameNewsCommand(chatId, userName);
        } else if (message.equals(Command.NEWS_ANIME.toLowerCase())) {
            animeNewsCommand(chatId, userName);
        } else if (message.equals(Command.SUBSCRIBE_KZ_INFORMBURO.toLowerCase())) {
            subscribeCommand(user, SubscriptionType.KZ_INFORMBURO);
        } else if (message.equals(Command.UNSUBSCRIBE_KZ_INFORMBURO.toLowerCase())) {
            unsubscribeCommand(user, SubscriptionType.KZ_INFORMBURO);
        } else if (message.equals(Command.SUBSCRIBE_GAMES_STOPGAME.toLowerCase())) {
            subscribeCommand(user, SubscriptionType.GAME_STOPGAME);
        } else if (message.equals(Command.UNSUBSCRIBE_GAMES_STOPGAME.toLowerCase())) {
            unsubscribeCommand(user, SubscriptionType.GAME_STOPGAME);
        } else if (message.equals(Command.SUBSCRIBE_ANIME_SHIKIMORI.toLowerCase())) {
            subscribeCommand(user, SubscriptionType.ANIME_SHIKIMORI);
        } else if (message.equals(Command.UNSUBSCRIBE_ANIME_SHIKIMORI.toLowerCase())) {
            unsubscribeCommand(user, SubscriptionType.ANIME_SHIKIMORI);
        }
    }

    @Override
    public String getBotUsername() {
        return "SpringNews";
    }

    private void startCommand(Long chatId, String userName) {
        var text = """
                Добро пожаловать, %s! 👋
                
                Мы рады приветствовать вас в нашем новостном боте! 📰
                
                Вы хотите всегда быть в курсе последних новостей и событий? Мы предлагаем вам уникальную возможность подписаться на различные новостные каталоги, которые будут регулярно обновлять вас актуальными новостями из разных областей.
                
                Вы можете подписаться на следующие новости:
                %s
                %s
                %s
                
                Выберите интересующий вас каталог и мы начнем отправлять вам самые актуальные новости по этой теме.
                
                Чтобы подписаться, просто нажмите на соответствующую кнопку с номером каталога. Вы можете подписаться на несколько каталогов одновременно или изменить свой выбор в любое время.
                
                Не упустите возможность быть в курсе событий! 📩
                """;

        var formattedText = String.format(text, userName, Command.NEWS_KZ, Command.NEWS_GAMES, Command.NEWS_ANIME);

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(formattedText);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add(Command.SUBSCRIPTIONS);
        KeyboardRow row2 = new KeyboardRow();
        row2.add(Command.NEWS_KZ);
        KeyboardRow row3 = new KeyboardRow();
        row3.add(Command.NEWS_GAMES);
        KeyboardRow row4 = new KeyboardRow();
        row4.add(Command.NEWS_ANIME);

        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboard.add(row4);

        keyboardMarkup.setKeyboard(keyboard);

        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void homeCommand(User user) {
        SendMessage message = new SendMessage();
        message.setChatId(user.getTelegramId());
        message.setText("Выберите следующие действия: ");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add(Command.SUBSCRIPTIONS);
        KeyboardRow row2 = new KeyboardRow();
        row2.add(Command.NEWS_KZ);
        KeyboardRow row3 = new KeyboardRow();
        row3.add(Command.NEWS_GAMES);
        KeyboardRow row4 = new KeyboardRow();
        row4.add(Command.NEWS_ANIME);

        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboard.add(row4);

        keyboardMarkup.setKeyboard(keyboard);

        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void subscriptionsCommand(User user) {
        List<UserSubscription> subscriptions = user.getSubscriptions();

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Мои подписки: \n");
        for (UserSubscription subscription : subscriptions) {
            if(subscription.getActive()) {
                stringBuilder.append(subscription.getType().toText()).append("\n");
            }
        }

        SendMessage message = new SendMessage();
        message.setChatId(user.getTelegramId());
        message.setText(stringBuilder.toString());

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();

        row1.add(Command.Home);
        keyboard.add(row1);
        for (UserSubscription subscription : subscriptions) {
            if(subscription.getActive()) {
                KeyboardRow row = new KeyboardRow();
                if (subscription.getType() == SubscriptionType.KZ_INFORMBURO) {
                    row.add(Command.UNSUBSCRIBE_KZ_INFORMBURO);
                } else if (subscription.getType() == SubscriptionType.GAME_STOPGAME) {
                    row.add(Command.UNSUBSCRIBE_GAMES_STOPGAME);
                }
                else if (subscription.getType() == SubscriptionType.ANIME_SHIKIMORI) {
                    row.add(Command.UNSUBSCRIBE_ANIME_SHIKIMORI);
                }
                keyboard.add(row);
            }
        }


        keyboardMarkup.setKeyboard(keyboard);

        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    private void kzNewsCommand(Long chatId, String userName) {
        var text = "Выберите интересующий сервис для подписки на новости Казахстана:";

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add(Command.Home);
        KeyboardRow row2 = new KeyboardRow();
        row2.add(Command.SUBSCRIBE_KZ_INFORMBURO);

        keyboard.add(row1);
        keyboard.add(row2);

        keyboardMarkup.setKeyboard(keyboard);

        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    private void gameNewsCommand(Long chatId, String userName) {
        var text = "Выберите интересующий сервис для подписки на игровые новости:";

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add(Command.Home);
        KeyboardRow row2 = new KeyboardRow();
        row2.add(Command.SUBSCRIBE_GAMES_STOPGAME);

        keyboard.add(row1);
        keyboard.add(row2);

        keyboardMarkup.setKeyboard(keyboard);

        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void animeNewsCommand(Long chatId, String userName) {
        var text = "Выберите интересующий сервис для подписки на новости по аниме:";

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add(Command.Home);
        KeyboardRow row2 = new KeyboardRow();
        row2.add(Command.SUBSCRIBE_ANIME_SHIKIMORI);

        keyboard.add(row1);
        keyboard.add(row2);

        keyboardMarkup.setKeyboard(keyboard);

        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void subscribeCommand(User user, SubscriptionType type) {
        userSubscriptionService.subscribeUserToNews(user, type);

        var text = "Спасибо за подписку на <b>%s</b>";

        var formattedText = String.format(text, type.toText());

        SendMessage message = new SendMessage();
        message.setChatId(user.getTelegramId());
        message.setParseMode(ParseMode.HTML);
        message.setText(formattedText);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void unsubscribeCommand(User user, SubscriptionType type) {
        userSubscriptionService.unsubscribeUserFromNews(user, type);

        var text = "Вы отписались от <b>%s</b>";

        var formattedText = String.format(text, type.toText());

        SendMessage message = new SendMessage();
        message.setChatId(user.getTelegramId());
        message.setParseMode(ParseMode.HTML);
        message.setText(formattedText);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendNewsToSubscribers(SubscriptionType type) {
        News news = newsService.getNewsToSubscribers(type);

        if (news == null) return;

        List<UserSubscription> subscribers = userSubscriptionService.getAllSubscribersTo(type);

        String baseText = """
            <blockquote>Цитируем <b>%s</b></blockquote>
            <b>%s</b>
            
            %s
            """;
        String formattedText = String.format(baseText, type.toText(), news.getTitle(), news.getContent());

        int chunkSize = 4000;

        for (UserSubscription subscriber : subscribers) {
            User user = subscriber.getUser();

            if (!subscriber.getActive()) return;

            SendMessage message = new SendMessage();
            message.setChatId(user.getTelegramId());
            message.setParseMode(ParseMode.HTML);

            // Split the message into chunks
            for (int i = 0; i < formattedText.length(); i += chunkSize) {
                int endIndex = Math.min(i + chunkSize, formattedText.length());
                String chunk = formattedText.substring(i, endIndex);

                message.setText(chunk);

                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }

        news.setSend(true);
        newsService.saveNews(news);
    }


    @Scheduled(fixedDelay = 60000) // 1 min
    public void sendKzInformburoToSubscribers() {
        sendNewsToSubscribers(SubscriptionType.KZ_INFORMBURO);
    }

    @Scheduled(fixedDelay = 60000) // 1 min
    public void sendGameStopGameToSubscribers() {
        sendNewsToSubscribers(SubscriptionType.GAME_STOPGAME);
    }

    @Scheduled(fixedDelay = 60000) // 1 min
    public void sendAnimeShikimoriToSubscribers() {
        sendNewsToSubscribers(SubscriptionType.ANIME_SHIKIMORI);
    }
}

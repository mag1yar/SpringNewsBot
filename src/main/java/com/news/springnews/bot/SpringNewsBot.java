package com.news.springnews.bot;

import com.news.springnews.constants.Command;
import com.news.springnews.parser.GameStopGameNewsParser;
import com.news.springnews.parser.KzInformburoNewsParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
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
    private static KzInformburoNewsParser kzInformburoNewsParser;
    private static GameStopGameNewsParser gameStopGameNewsParser;

    public SpringNewsBot(@Value("${bot.token}") String token) {
        super(token);
        kzInformburoNewsParser = new KzInformburoNewsParser();
        gameStopGameNewsParser = new GameStopGameNewsParser();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(!update.hasMessage() || !update.getMessage().hasText()) return;

        var message = update.getMessage().getText();
        var chatId = update.getMessage().getChatId();
        var userName = update.getMessage().getChat().getUserName();

        switch (message) {
            case Command.START -> {
                startCommand(chatId, userName);
            }
            case Command.NEWS_KZ -> {
                kzNewsCommand(chatId, userName);
            }
            case Command.NEWS_GAMES -> {
                gameNewsCommand(chatId, userName);
            }
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
                
                Выберите интересующий вас каталог и мы начнем отправлять вам самые актуальные новости по этой теме.
                
                Чтобы подписаться, просто нажмите на соответствующую кнопку с номером каталога. Вы можете подписаться на несколько каталогов одновременно или изменить свой выбор в любое время.
                
                Не упустите возможность быть в курсе событий! 📩
                """;

        var formattedText = String.format(text, userName, Command.NEWS_KZ, Command.NEWS_GAMES);

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(formattedText);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        row.add(Command.NEWS_KZ);
        row.add(Command.NEWS_GAMES);

        keyboard.add(row);

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
        KeyboardRow row = new KeyboardRow();

        row.add(Command.SUBSCRIBE_KZ);

        keyboard.add(row);

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
        KeyboardRow row = new KeyboardRow();

        row.add(Command.SUBSCRIBE_GAMES);

        keyboard.add(row);

        keyboardMarkup.setKeyboard(keyboard);

        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

//    private void kzNewsCommand(Long chatId, String userName) {
//        LOG.info(chatId + " " + userName);
//
//        var titles = SpringNewsParser.parseKzNewsTitles();
//
//        var text = "Новости за текущий момент:\n";
//        if (titles.isEmpty()) {
//            text = "Простите, что-то пошло не так, попробуйте позже.";
//        } else {
//            for (int i = 0; i < titles.size(); i++) {
//                text += (i + 1) + ". " + titles.get(i) + "\n";
//            }
//        }
//
//        SendMessage message = new SendMessage();
//        message.setChatId(chatId);
//        message.setText(text);
//
//        try {
//            execute(message);
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//    }
//    private void gameNewsCommand(Long chatId, String userName) {
//        LOG.info(chatId + " " + userName);
//
//        var titles = SpringNewsParser.parseGameNewsTitles();
//
//        var text = "Игровые новости за текущий момент:\n";
//        if (titles.isEmpty()) {
//            text = "Простите, что-то пошло не так, попробуйте позже.";
//        } else {
//            for (int i = 0; i < titles.size(); i++) {
//                text += (i + 1) + ". " + titles.get(i) + "\n";
//            }
//        }
//
//        SendMessage message = new SendMessage();
//        message.setChatId(chatId);
//        message.setText(text);
//
//        try {
//            execute(message);
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//    }

    private void subscribeToKzNews(Long chatId, String userName) {
        var text = "Выберите сервис для подписки на новости Казахстана:";

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        // Добавляем кнопки для подписки
        row.add("Informburo");

        keyboard.add(row);

        keyboardMarkup.setKeyboard(keyboard);

        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void subscribeToGameNews(Long chatId, String userName) {
        var text = "Выберите сервис для подписки на игровые новости:";

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        // Добавляем кнопки для подписки
        row.add("StopGame");

        keyboard.add(row);

        keyboardMarkup.setKeyboard(keyboard);

        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}

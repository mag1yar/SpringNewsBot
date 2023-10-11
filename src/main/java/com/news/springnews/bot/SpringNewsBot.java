package com.news.springnews.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class SpringNewsBot extends TelegramLongPollingBot {

    private static final Logger LOG = LoggerFactory.getLogger(SpringNewsBot.class);

    private static final String COMMAND_START = "/start";
    private static final String COMMAND_HELP = "/help";


    public SpringNewsBot(@Value("${bot.token}") String token) {
        super(token);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(!update.hasMessage() || !update.getMessage().hasText()) return;

        var message = update.getMessage().getText();
        var chatId = update.getMessage().getChatId();
        var userName = update.getMessage().getChat().getUserName();

        switch (message) {
            case COMMAND_START -> {
                startCommand(chatId, userName);
            }
        }

    }

    @Override
    public String getBotUsername() {
        return "SpringNews";
    }

    private void sendMessage(Long chatId, String text) {
        var chatIdStr = String.valueOf(chatId);
        var sendMessage = new SendMessage(chatIdStr, text);
        try {
            execute(sendMessage);
        }catch (TelegramApiException e) {
            LOG.error("Ошибка отправки сообщения", e);
        }
    }

    private void startCommand(Long chatId, String userName) {
        LOG.info(chatId + " " + userName );
        var text = """
                Добро пожаловать, %s! 👋
                                
                Мы рады приветствовать вас в нашем новостном боте! 📰
                                
                Вы хотите всегда быть в курсе последних новостей и событий? Мы предлагаем вам уникальную возможность подписаться на различные новостные каталоги, которые будут регулярно обновлять вас актуальными новостями из разных областей.
                                
                У нас есть подборки новостей по следующим темам:
                1. 🌍 Мировые события
                2. 📰 Политика
                3. 💼 Бизнес
                4. 🎬 Развлечения
                5. 🏞️ Путешествия
                6. 🎨 Искусство и культура
                7. 🏈 Спорт
                8. 📱 Технологии
                9. 🌱 Наука и здоровье
                                
                Выберите интересующий вас каталог и мы начнем отправлять вам самые актуальные новости по этой теме.
                                
                Чтобы подписаться, просто нажмите на соответствующую кнопку с номером каталога. Вы можете подписаться на несколько каталогов одновременно или изменить свой выбор в любое время.
                                
                Не упустите возможность быть в курсе событий! 📩
                """;

        var formattedText = String.format(text, userName);
        sendMessage(chatId, formattedText);
    }
}

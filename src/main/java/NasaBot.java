import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class NasaBot extends TelegramLongPollingBot {
    String url = "https://api.nasa.gov/planetary/apod?api_key=4moStbvVgSKTv3cjUVLDFtqBCyOOIY5g48TeChWV";
    String botName;
    String botToken;

    public NasaBot(String botName, String botToken) {
        this.botName = botName;
        this.botToken = botToken;
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        // TODO
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            String[] splittedText = text.split(" ");
            String option = splittedText[0];
            Long chatId = update.getMessage().getChatId();

            switch (option) {
                case "/start":
                    sendMessage(chatId, "Это бот Наса. Я присылаю картинку дня.");
                    break;
                case "/help":
                    sendMessage(chatId, "Введите /image - для получения картинки дня.");
                    sendMessage(chatId, "Введите /date гггг-мм-дд - для получения картинки дня в определённую дату.");
                    break;
                case "/image":
                    String image = Utils.getImageUrl(url);
                    sendMessage(chatId, image);
                    break;
                case "/date":
                    image = Utils.getImageUrl(url + "&date=" + splittedText[1]);
                    sendMessage(chatId, image);
                    break;
                default:
                    sendMessage(chatId, "Не знаю такой команды. Введите /help - для помощи.");
            }
        }
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        // TODO
        return botName;
    }

    @Override
    public String getBotToken() {
        // TODO
        return botToken;
    }
}


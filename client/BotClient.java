package com.javarush.test.level30.lesson15.big01.client;

import com.javarush.test.level30.lesson15.big01.ConsoleHelper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * BotClient class
 * date: 21.05.2016
 *
 * @autor TheZalesskie
 */
public class BotClient extends Client {

    private static int botsCounter = 0;

    public static void main(String[] args) {
        new BotClient().run();
    }

    public class BotSocketThread extends SocketThread {
        @Override
        protected void clientMainLoop() throws IOException, ClassNotFoundException {
            sendTextMessage("Привет чатику. Я бот. Понимаю команды: дата, день, " +
                    "месяц, год, время, час, минуты, секунды.");
            super.clientMainLoop();
        }

        @Override
        protected void processIncomingMessage(String message) {
            // Вывести в консоль текст полученного сообщения message
            ConsoleHelper.writeMessage(message);

            // Получить из message имя отправителя и текст сообщения. Они разделены ": "
            String senderName = " ";
            String senderMessageText;

            if (message.contains(": ")) {
                senderName = message.substring(0, message.indexOf(": "));
                senderMessageText = message.substring(message.indexOf(": ") + 2);
            } else {
                senderMessageText = message;
            }

            SimpleDateFormat dateFormat = null;
            // Отправить ответ в зависимости от текста принятого сообщения. Если текст сообщения:
            if("дата".equalsIgnoreCase(senderMessageText)){
                dateFormat = new SimpleDateFormat("d.MM.YYYY");
            }else if ("день".equalsIgnoreCase(senderMessageText)){
                dateFormat = new SimpleDateFormat("d");
            }else if ("месяц".equalsIgnoreCase(senderMessageText)) {
                dateFormat = new SimpleDateFormat("MMMM");
            }else if ("год".equalsIgnoreCase(senderMessageText)) {
                dateFormat = new SimpleDateFormat("YYYY");
            }else if ("время".equalsIgnoreCase(senderMessageText)) {
                dateFormat = new SimpleDateFormat("H:mm:ss");
            }else if ("час".equalsIgnoreCase(senderMessageText)) {
                dateFormat = new SimpleDateFormat("H");
            }else if ("минуты".equalsIgnoreCase(senderMessageText)) {
                dateFormat = new SimpleDateFormat("m");
            }else if ("секунды".equalsIgnoreCase(senderMessageText)) {
                dateFormat = new SimpleDateFormat("s");
            }
            if (dateFormat != null)
            {
                sendTextMessage("Информация для " + senderName + ": " + dateFormat.format(Calendar.getInstance().getTime()));
            }

        }
    }

    @Override
    protected SocketThread getSocketThread() {
        //Он должен создавать и возвращать объект класса BotSocketThread
        return new BotSocketThread();
    }


    @Override
    protected boolean shouldSentTextFromConsole() {
        //Он должен всегда возвращать false. Мы не хотим, чтобы бот отправлял текст введенный в консоль.
        return false;
    }

    @Override
    // метод должен генерировать новое имя бота, например: date_bot_XX, где XX – любое число от 0 до 99.
    // Этот метод должен возвращать каждый раз новое значение, на случай, если на сервере захотят зарегистрироваться несколько
    protected String getUserName() {
        if (botsCounter == 99) {
            botsCounter = 0;
        }

        return "date_bot_" + botsCounter++;
    }

}

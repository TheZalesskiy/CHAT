package client;



import console.ConsoleHelper;

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
            sendTextMessage("Hello chatik . I bot . I understand the team : date, day , " +
" Month, year , time , hour , minute, second");
            super.clientMainLoop();
        }

        @Override
        protected void processIncomingMessage(String message) {
            // Display text in the console message received message
            ConsoleHelper.writeMessage(message);

            // Get the message from the sender's name and a message. They are separated by ":"
            String senderName = " ";
            String senderMessageText;

            if (message.contains(": ")) {
                senderName = message.substring(0, message.indexOf(": "));
                senderMessageText = message.substring(message.indexOf(": ") + 2);
            } else {
                senderMessageText = message;
            }

            SimpleDateFormat dateFormat = null;
            // Send the answer depending on the text of the received message. If the message text:
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
                sendTextMessage("info for " + senderName + ": " + dateFormat.format(Calendar.getInstance().getTime()));
            }

        }
    }

    @Override
    protected SocketThread getSocketThread() {
        //He should create and return an object of class BotSocketThread
        return new BotSocketThread();
    }


    @Override
    protected boolean shouldSentTextFromConsole() {
        //It must always return false. We do not want the bot to send the text entered into the console.
        return false;
    }

    @Override
    /**
     * method should generate a new bot's name, for example: date_bot_XX, where XX - any number from 0 to 99.
      * This method should return each time a new value, in case you want to create an account on the server several
     */
    protected String getUserName() {
        if (botsCounter == 99) {
            botsCounter = 0;
        }

        return "date_bot_" + botsCounter++;
    }

}

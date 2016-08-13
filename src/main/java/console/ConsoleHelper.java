package console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * console.ConsoleHelper class
 * helper class to read or write to the console
 * 17.05.2016
 * Autor by TheZalesskie
 */
public class ConsoleHelper {
    private static BufferedReader reader =
            new BufferedReader(new InputStreamReader(System.in));

    //methods
    //message in console
    public static void writeMessage(String message) {
        System.out.println(message);
    }

    //read text message - I must read a line from the console
    public static String readString() {
        String message;
        while (true) {
            try {
                message = reader.readLine();
                break;
                //If while reading the exception occurred , bring the user a message
            } catch (IOException e) {
                System.out.println("An error occurred while trying to enter text . " +
                        "Try again.");
            }
        }

        return message;
    }

    //read number - should return the number entered
    public static int readInt() {
        int i;
        while (true) {
            try {
                i = Integer.parseInt(readString());
                break;
                //
            } catch (NumberFormatException e) {
                System.out.println("An error occurred while trying to enter the number. " +
"Try again");
            }
        }

        return i;


    }
}

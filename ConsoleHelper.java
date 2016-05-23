package com.javarush.test.level30.lesson15.big01;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * ConsoleHelper class
 * вспомогательный класс, для чтения или записи в консоль
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

    //read text message - должен считывать строку с консоли
    public static String readString() {
        String message;
        while (true) {
            try {
                message = reader.readLine();
                break;
                //Если во время чтения произошло исключение, вывести пользователю сообщение
            } catch (IOException e) {
                System.out.println("Произошла ошибка при попытке ввода текста. " +
                        "Попробуйте еще раз.");
            }
        }

        return message;
    }

    //read number - должен возвращать введенное число
    public static int readInt() {
        int i;
        while (true) {
            try {
                i = Integer.parseInt(readString());
                break;
                //
            } catch (NumberFormatException e) {
                System.out.println("Произошла ошибка при попытке ввода числа. " +
                        "Попробуйте еще раз.");
            }
        }

        return i;


    }
}

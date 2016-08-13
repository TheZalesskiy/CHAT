package com.javarush.test.level30.lesson15.big01.client;

import java.util.*;

/**
 * ClientGuiModel class
 * date: 21.05.2016
 *
 * @autor TheZalesskie
 */
public class ClientGuiModel {

    // Add to it the set (set) strings as constant field allUserNames
    private final Set<String> allUserNames = new HashSet<>();

    // Add Field String newMessage, which will contain a new message , which was received by the client
    private String newMessage;


    /** methods **/
    // Add method void addUser (String newUserName), which must be added the name of the participant in a variety of storing all participants
    public void addUser(String newUserName) {

        allUserNames.add(newUserName);
    }


    // Add method void deleteUser (String userName), which will remove the participant's name from the set
    public void deleteUser(String userName) {

        allUserNames.remove(userName);
    }


    /** getters and setters **/
    // Add the getter for allUserNames, prohibiting to modify the returned set.
    // Look at how this can be done using the Collections class method
    public Set<String> getAllUserNames() {

        return Collections.unmodifiableSet(allUserNames);
    }

    public String getNewMessage() {
        return newMessage;
    }

    public void setNewMessage(String newMessage) {
        this.newMessage = newMessage;
    }
}

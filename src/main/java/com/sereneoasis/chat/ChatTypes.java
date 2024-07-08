package com.sereneoasis.chat;

import java.util.Arrays;

/***
 * Represents all distinct allowable types of conversation elements
 */
public enum ChatTypes {
    STATEMENT("statement"),
    QUESTION("question"),

    TERMINAL("terminal"),

    WALK_TO("walk_to");

    private final String string;


    ChatTypes(String string) {
        this.string = string;
    }

    public static ChatTypes getFromString(String s) {
        return Arrays.stream(ChatTypes.values()).filter(chatTypes -> chatTypes.toString().equals(s)).findAny().get();
    }

    public String toString() {
        return string;
    }

}

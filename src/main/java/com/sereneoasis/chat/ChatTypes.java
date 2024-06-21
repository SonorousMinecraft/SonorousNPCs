package com.sereneoasis.chat;

import java.util.Arrays;

public enum ChatTypes {
    STATEMENT("statement"),
    QUESTION("question"),

    TERMINAL("terminal");

    private String string;


    public static ChatTypes getFromString(String s){
        return Arrays.stream(ChatTypes.values()).filter(chatTypes -> chatTypes.toString().equals(s)).findAny().get();
    }

    public String toString() {
        return string;
    }

    ChatTypes(String string){
        this.string = string;
    }

}

package com.sereneoasis.config;

import java.io.File;

public class FileManager {

    private final File mainDir, schemDir, chatDir;

    public File getChatDir() {
        return chatDir;
    }

    public FileManager(){
        mainDir = getOrCreateDir("SereneNPCs");
        schemDir = getOrCreateDir("Schematics", mainDir);
        chatDir = getOrCreateDir("Chats", mainDir);
    }

    public File[] getSchematics(){
        return schemDir.listFiles();
    }

    private static File getOrCreateDir(String name){
        File file = new File(name);
        if (!file.isDirectory()) {
            file.mkdir();
        }
        return file;
    }

    private static File getOrCreateDir(String name, File parent){
        File file = new File(parent, name);
        if (!file.isDirectory()) {
            file.mkdir();
        }
        return file;
    }
}

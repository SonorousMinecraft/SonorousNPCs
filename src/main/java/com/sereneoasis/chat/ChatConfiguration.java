package com.sereneoasis.chat;

import com.sereneoasis.config.ConfigFile;
import com.sereneoasis.npc.types.NPCTypes;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;

public class ChatConfiguration {

    public  ChatConfiguration(){
        Arrays.stream(NPCTypes.values()).forEach(npcTypes -> {
            ConfigFile chat = new ConfigFile(npcTypes.toString());
            FileConfiguration configuration = chat.getConfig();
            configuration.addDefault("0.TYPE", ChatTypes.STATEMENT.toString());
            configuration.addDefault("0.TEXT", "Hello! I am an NPC.");
            configuration.addDefault("0.NEXT", "1");

            configuration.addDefault("1.TYPE", ChatTypes.STATEMENT.toString());
            configuration.addDefault("1.TEXT", "I will be guiding you on your journey.");
            configuration.addDefault("1.NEXT", "2");

            configuration.addDefault("2.TYPE", ChatTypes.QUESTION.toString());
            configuration.addDefault("2.TEXT", "Does this sound good?");
            configuration.addDefault("2.Y", "3");
            configuration.addDefault("2.N", "4");

            configuration.addDefault("3.TYPE", ChatTypes.STATEMENT.toString());
            configuration.addDefault("3.TEXT", "Ok, let's begin.");
            configuration.addDefault("3.NEXT", "2");

            configuration.addDefault("4.TYPE", ChatTypes.TERMINAL.toString());
            configuration.addDefault("4.TEXT", "Sorry to hear that.");
            chat.saveConfig();
        });
    }
}

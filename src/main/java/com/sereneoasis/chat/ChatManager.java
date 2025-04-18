package com.sereneoasis.chat;

import com.sereneoasis.chat.builders.ChatBuilder;
import com.sereneoasis.config.ConfigFile;
import com.sereneoasis.npc.random.types.NPCTypes;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.HashMap;

/***
 * Parses the .yml files directly and sends to the ChatBuilder
 */
public class ChatManager {

    private final HashMap<NPCTypes, ChatBuilder> NPCTypesChatBuilders = new HashMap<>();

    public ChatManager() {
        Arrays.stream(NPCTypes.values()).forEach(npcTypes -> {
            ConfigFile chat = new ConfigFile(npcTypes.toString());
            FileConfiguration configuration = chat.getConfig();

            int currentIndex = 0;
            ChatBuilder chatBuilder = new ChatBuilder();
            while (configuration.contains(String.valueOf(currentIndex))) {
                ChatTypes type = ChatTypes.getFromString(configuration.get(currentIndex + ".TYPE").toString());

                String text = configuration.get(currentIndex + ".TEXT").toString();

                switch (type) {
                    case STATEMENT -> {
                        int next = Integer.valueOf(configuration.get(currentIndex + ".NEXT").toString());
                        chatBuilder.addStatement(text, next);
                    }
                    case QUESTION -> {
                        int yesNext = Integer.valueOf(configuration.get(currentIndex + ".Y").toString());
                        int noNext = Integer.valueOf(configuration.get(currentIndex + ".N").toString());
                        chatBuilder.addQuestion(text, yesNext, noNext);
                    }
                    case TERMINAL -> {
                        chatBuilder.addTerminal(text);
                    }
                }
                currentIndex += 1;
            }
            ;
            NPCTypesChatBuilders.put(npcTypes, chatBuilder);

        });
    }

    public ChatBuilder getChatBuilder(NPCTypes types) {
        return NPCTypesChatBuilders.get(types);
    }


}

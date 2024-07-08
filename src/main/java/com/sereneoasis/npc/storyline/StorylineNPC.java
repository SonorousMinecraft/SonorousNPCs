package com.sereneoasis.npc.storyline;

import com.mojang.authlib.GameProfile;
import com.sereneoasis.chat.ChatTypes;
import com.sereneoasis.chat.builders.ChatBuilder;
import com.sereneoasis.config.ConfigFile;
import com.sereneoasis.entity.SereneHumanEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.UUID;

/***
 * More complex versions of BasicNPC
 * These have a pivotal role in the storyline of the Server
 */
public abstract class StorylineNPC extends SereneHumanEntity {


    private static final HashMap<UUID, ChatBuilder> UUID_CHAT_BUILDER_HASH_MAP = new HashMap<>();
    private static final HashMap<UUID, StorylineNPC> UUID_STORYLINE_NPC_HASH_MAP = new HashMap<>();

    public StorylineNPC(MinecraftServer server, ServerLevel world, GameProfile profile, ClientInformation clientOptions, String conversationFile) {
        super(server, world, profile, clientOptions);


        ConfigFile chat = new ConfigFile(conversationFile);
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
                case WALK_TO -> {
                    String walkTo = configuration.get(currentIndex + ".GOAL").toString();
                    int next = Integer.valueOf(configuration.get(currentIndex + ".NEXT").toString());
                    chatBuilder.addWalkTo(text, walkTo, next);
                }

            }
            currentIndex += 1;
        }
        ;
        UUID_CHAT_BUILDER_HASH_MAP.put(this.getUUID(), chatBuilder);
        UUID_STORYLINE_NPC_HASH_MAP.put(this.getUUID(), this);
    }

    public static ChatBuilder getChat(UUID uuid) {
        return UUID_CHAT_BUILDER_HASH_MAP.get(uuid);
    }

    public static StorylineNPC getInstance(UUID uuid) {
        return UUID_STORYLINE_NPC_HASH_MAP.get(uuid);
    }

    @Override
    public void tick() {
        super.tick();

    }
}











package com.sereneoasis.npc.storyline;

import com.mojang.authlib.GameProfile;
import com.sereneoasis.SereneNPCs;
import com.sereneoasis.chat.ChatTypes;
import com.sereneoasis.chat.builders.ChatBuilder;
import com.sereneoasis.config.ConfigFile;
import com.sereneoasis.config.FileManager;
import com.sereneoasis.entity.AI.goal.complex.combat.KillTargetEntity;
import com.sereneoasis.entity.HumanEntity;
import com.sereneoasis.utils.Vec3Utils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

public abstract class StorylineNPC extends HumanEntity {


    private static final HashMap<UUID, ChatBuilder>UUID_CHAT_BUILDER_HASH_MAP = new HashMap<>();
    private static final HashMap<UUID, StorylineNPC>UUID_STORYLINE_NPC_HASH_MAP = new HashMap<>();

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
                    int next = Integer.valueOf(configuration.get(currentIndex + ".NEXT").toString());
                    chatBuilder.addWalkTo(text, next);
                }

            }
            currentIndex += 1;
        };
        UUID_CHAT_BUILDER_HASH_MAP.put(this.getUUID(), chatBuilder);
        UUID_STORYLINE_NPC_HASH_MAP.put(this.getUUID(), this);
    }

    @Override
    public void tick() {
        super.tick();

//        if (!masterGoalSelector.doingGoal("kill hostile entity")) {
//            if (targetSelector.retrieveTopHostile() instanceof LivingEntity hostile && (!Vec3Utils.isObstructed(this.getPosition(0), hostile.getPosition(0), this.level()))) {
//                masterGoalSelector.addMasterGoal(new KillTargetEntity("kill hostile entity", this, hostile));
//            } else {
////                if (!masterGoalSelector.doingGoal("roam")) {
////                    masterGoalSelector.addMasterGoal(new RandomExploration("roam", this, null));
////                }
//                if (!inventoryTracker.hasEnoughFood()) {
//                    if (!masterGoalSelector.doingGoal("kill food entity")) {
//                        if (targetSelector.retrieveTopPeaceful() instanceof LivingEntity peaceful) {
//                            masterGoalSelector.addMasterGoal(new KillTargetEntity("kill food entity", this, peaceful));
//                        }
//                    }
//                } else if (inventoryTracker.hasFood()) {
//                    this.eat(this.level(), inventoryTracker.getMostAppropriateFood());
//                }
//            }
//        }
    }


    public static ChatBuilder getChat(UUID uuid){
        return UUID_CHAT_BUILDER_HASH_MAP.get(uuid);
    }

    public static StorylineNPC getInstance(UUID uuid){
        return UUID_STORYLINE_NPC_HASH_MAP.get(uuid);
    }
}











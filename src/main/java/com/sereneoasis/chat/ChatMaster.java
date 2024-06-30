package com.sereneoasis.chat;

import com.sereneoasis.chat.builders.ChatBuilder;
import com.sereneoasis.npc.storyline.StorylineNPC;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ChatMaster {

    private static final HashMap<UUID, ChatMaster>UUID_CHAT_MASTER_HASH_MAP = new HashMap<>();
    private static final HashMap<UUID, StorylineNPC>UUID_STORYLINE_NPC_HASH_MAP = new HashMap<>();

    public static ChatMaster getInstance(Player player){
        return UUID_CHAT_MASTER_HASH_MAP.get(player.getUniqueId());
    }

    public static StorylineNPC getNPC(Player player){

        return UUID_STORYLINE_NPC_HASH_MAP.get(player.getUniqueId());
    }
    private Set<ChatBuilder>chatBuilders= new HashSet<>();

    private ChatBuilder current;

    public ChatMaster(Player player){
        current = new ChatBuilder();
        chatBuilders.add(current);
        current.openChat(player);
        UUID_CHAT_MASTER_HASH_MAP.put(player.getUniqueId(),this);
    }

    public ChatMaster(Player player, ChatBuilder chatBuilder){
        current = chatBuilder;
        chatBuilders.add(current);
        current.openChat(player);
        UUID_CHAT_MASTER_HASH_MAP.put(player.getUniqueId(),this);
    }

    public ChatMaster(Player player, ChatBuilder chatBuilder, StorylineNPC storylineNPC){
        current = chatBuilder;
        chatBuilders.add(current);
        current.openChat(player);
        UUID_CHAT_MASTER_HASH_MAP.put(player.getUniqueId(),this);
        UUID_STORYLINE_NPC_HASH_MAP.put(player.getUniqueId(),storylineNPC);
    }

    public void next(Player player, int next){
        current.next(player, next);
    }

    public void next(Player player){
        current.next(player);
    }
}

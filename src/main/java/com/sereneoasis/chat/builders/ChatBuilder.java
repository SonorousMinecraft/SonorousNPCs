package com.sereneoasis.chat.builders;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatBuilder{

    private List<ChatUnit> chatUnitArrayList = new ArrayList<>();
    private int current;

    public ChatBuilder(){

    }

    public void addStatement(String text, int next){
        TextComponent message = new TextComponent(text);
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/SereneNPCs next " + next));

        ChatUnit chatUnit = new ChatUnit(message);
        chatUnitArrayList.add(chatUnit);
    }

    public void addQuestion(String text, int yesNext, int noNext){
        TextComponent message = new TextComponent(text);
        TextComponent yes = new TextComponent("yes");
        yes.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/SereneNPCs next " + yesNext));

        TextComponent no = new TextComponent("no");
        no.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/SereneNPCs next " + noNext));

        message.addExtra(yes);
        message.addExtra(no);
        ChatUnit chatUnit = new ChatUnit(message);
        chatUnitArrayList.add(chatUnit);
    }

    public void addTerminal(String text) {
        TextComponent message = new TextComponent(text);
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/SereneNPCs stop"));

        ChatUnit chatUnit = new ChatUnit(message);
        chatUnitArrayList.add(chatUnit);
    }

    public void next(Player player, int next){
        current = next;
        player.spigot().sendMessage(chatUnitArrayList.get(current).getChat());

    }

    public void openChat(Player player){
        current = 0;
        player.spigot().sendMessage(chatUnitArrayList.get(0).getChat());
    }

    private class ChatUnit{

        private BaseComponent[] chat;

        public BaseComponent[] getChat() {
            return chat;
        }

        ChatUnit(TextComponent... textComponents){
            ComponentBuilder builder  = new ComponentBuilder();
            Arrays.stream(textComponents).forEach(builder::append);
            chat = builder.create();
        }

    }
}

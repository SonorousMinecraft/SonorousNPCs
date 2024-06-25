package com.sereneoasis.chat.builders;

import net.md_5.bungee.api.ChatColor;
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
        message.setColor(ChatColor.AQUA);
        message.setBold(true);
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/serenenpcs next " + next));

        ChatUnit chatUnit = new ChatUnit(message);
        chatUnitArrayList.add(chatUnit);
    }

    public void addQuestion(String text, int yesNext, int noNext){
        TextComponent message = new TextComponent(text);
        message.setColor(ChatColor.AQUA);
        message.setBold(true);

        message.addExtra("\n");
        TextComponent yes = new TextComponent("Yes");
        yes.setColor(ChatColor.GREEN);
        yes.setBold(true);
        yes.setUnderlined(true);
        yes.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/serenenpcs next " + yesNext));

        message.addExtra(yes);
        message.addExtra("\n");


        TextComponent no = new TextComponent("No");
        no.setColor(ChatColor.RED);
        no.setBold(true);
        no.setUnderlined(true);
        no.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/serenenpcs next " + noNext));

        message.addExtra(no);
        ChatUnit chatUnit = new ChatUnit(message);
        chatUnitArrayList.add(chatUnit);
    }

    public void addTerminal(String text) {
        TextComponent message = new TextComponent(text);
        message.setColor(ChatColor.YELLOW);
        message.setBold(true);

        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/serenenpcs stop"));

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

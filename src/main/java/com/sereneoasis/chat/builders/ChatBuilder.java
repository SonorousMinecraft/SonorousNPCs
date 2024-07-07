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

/***
 * Attaches functionality to text files used to create conversations and provides the UI
 */
public class ChatBuilder {

    private final List<ChatUnit> chatUnitArrayList = new ArrayList<>();
    private int current;

    /***
     * Builds a statement which requires a player to click to progress
     * @param text the statement
     * @param next the index of the next chat unit
     */
    public void addStatement(String text, int next) {
        TextComponent message = new TextComponent(text);
        message.setColor(ChatColor.AQUA);
        message.setBold(true);
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/serenenpcs next " + next));

        ChatUnit chatUnit = new ChatUnit(message);
        chatUnitArrayList.add(chatUnit);
    }

    /***
     * Builds a basic yes or no question
     * @param text the question
     * @param yesNext the index of the next chat unit if next
     * @param noNext the index of the next chat unit if no
     */
    public void addQuestion(String text, int yesNext, int noNext) {
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

    /***
     * Builds a statement which ends the conversation
     * @param text the final statement
     */
    public void addTerminal(String text) {
        TextComponent message = new TextComponent(text);
        message.setColor(ChatColor.YELLOW);
        message.setBold(true);

        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/serenenpcs stop"));

        ChatUnit chatUnit = new ChatUnit(message);
        chatUnitArrayList.add(chatUnit);
    }

    /***
     * Builds a statement which causes an NPC to navigate to a location when clicked
     * @param text the statement
     * @param goal the identifier for the location where the NPC will walk to
     * @param next the next chat unit index
     */
    public void addWalkTo(String text, String goal, int next) {
        TextComponent message = new TextComponent(text);
        message.setColor(ChatColor.YELLOW);
        message.setBold(true);

        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/serenenpcs walk_to " + goal));
//        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/serenenpcs next " + next));


        ChatUnit chatUnit = new ChatUnit(message);
        chatUnitArrayList.add(chatUnit);
    }

    /**
     * Progresses the conversation to a given index
     * @param player the player in the conversation
     * @param next the next chat unit index
     */
    public void next(Player player, int next) {
        current = next;
        player.spigot().sendMessage(chatUnitArrayList.get(current).getChat());
    }

    /**
     * Progresses the conversation to the next index
     * @param player the player in the conversation
     */
    public void next(Player player) {
        current = current + 1;
        player.spigot().sendMessage(chatUnitArrayList.get(current).getChat());

    }

    /***
     * Inits a conversation
     * @param player the player in the conversation
     */
    public void openChat(Player player) {
        current = 0;
        player.spigot().sendMessage(chatUnitArrayList.get(0).getChat());
    }

    /***
     * Represents a unit of conversation
     */
    private static class ChatUnit {

        private final BaseComponent[] chat;

        ChatUnit(TextComponent... textComponents) {
            ComponentBuilder builder = new ComponentBuilder();
            Arrays.stream(textComponents).forEach(builder::append);
            chat = builder.create();
        }

        public BaseComponent[] getChat() {
            return chat;
        }

    }
}

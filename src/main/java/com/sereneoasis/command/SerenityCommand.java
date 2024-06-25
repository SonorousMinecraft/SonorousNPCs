package com.sereneoasis.command;

import com.sereneoasis.SereneNPCs;
import com.sereneoasis.chat.ChatMaster;
import com.sereneoasis.utils.NPCUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SerenityCommand implements CommandExecutor {

//    private static final Set<ChatMaster> chatMasters = new HashSet<>();
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player player){

            if (strings.length == 0){
                SereneNPCs.plugin.addNPC(NPCUtils.spawnNPC(player.getLocation(), player, "noob", "Steve"));

            } else {
                switch (strings[0]) {
                    case "next" -> {
                        ChatMaster.getInstance(player).next(player, Integer.valueOf(strings[1]));
                    }
                    case "stop" -> {
                        TextComponent bye = new TextComponent("Bye bye!");
                        bye.setColor(ChatColor.GREEN);
                        bye.setBold(true);
                        bye.setUnderlined(true);
                        player.spigot().sendMessage(bye);
                    }
                    case "reload" -> {

                    }
                }
            }
        }
        return true;
    }
}
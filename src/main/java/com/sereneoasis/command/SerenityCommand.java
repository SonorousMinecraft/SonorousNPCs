package com.sereneoasis.command;

import com.sereneoasis.SereneNPCs;
import com.sereneoasis.chat.ChatMaster;
import com.sereneoasis.entity.AI.goal.complex.MasterGoal;
import com.sereneoasis.entity.AI.goal.complex.movement.NavigateToLocation;
import com.sereneoasis.npc.storyline.StorylineNPC;
import com.sereneoasis.utils.NPCUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.core.BlockPos;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SerenityCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player player) {

            if (strings.length == 0) {
                SereneNPCs.plugin.addNPC(NPCUtils.spawnNPC(player.getLocation(), player, "noob"));

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
                    case "walk_to" -> {
                        BlockPos goalPos = SereneNPCs.plugin.getStorylineLocations().getBlockPos(strings[1]);
                        StorylineNPC storylineNPC = ChatMaster.getNPC(player);

                        MasterGoal navigateToLocation = new NavigateToLocation("navigate", storylineNPC, blockPos -> goalPos.distSqr(blockPos) < 4, goalPos);
                        storylineNPC.getMasterGoalSelector().addMasterGoal(navigateToLocation);

                    }
                    case "reload" -> {

                    }
                    case "spawn" -> {
                        switch (strings[1]) {
                            case "guide" -> {
                                NPCUtils.spawnGuideNPC(player.getLocation(), player, "Guide", "Notch");
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}
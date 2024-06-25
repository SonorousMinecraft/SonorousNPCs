package com.sereneoasis.listeners;

import com.sereneoasis.SereneNPCs;
import com.sereneoasis.chat.ChatManager;
import com.sereneoasis.chat.ChatMaster;
import com.sereneoasis.chat.builders.ChatBuilder;
import com.sereneoasis.npc.guis.quests.QuestGUI;
import com.sereneoasis.npc.types.NPCTypes;
import com.sereneoasis.utils.ClientboundPlayerInfoUpdatePacketWrapper;
import com.sereneoasis.utils.EconUtils;
import com.sereneoasis.utils.PacketUtils;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class SereneListener implements Listener {


    private static final Random random = new Random();


    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Bukkit.getServer().getOnlinePlayers().forEach(player -> {
        SereneNPCs.plugin.getNpcs().forEach((serverPlayer) -> {
            ClientboundPlayerInfoUpdatePacketWrapper playerInfoPacket = new ClientboundPlayerInfoUpdatePacketWrapper(
                    EnumSet.of(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LATENCY),
                    serverPlayer,
                    180,
                    true
            );
            PacketUtils.sendPacket(playerInfoPacket.getPacket(), player);
        });
        });
        EconUtils.payPlayer(event.getPlayer(), 1000);

    }

    private final static Set<Player> PARRY_COOLDOWNS = new HashSet<>();


    @EventHandler
    public void onRightClickNPC(PlayerInteractEntityEvent event){

        Player player = event.getPlayer();
        if (player.isSneaking() ) {
            if (!PARRY_COOLDOWNS.contains(player)) {
                if (SereneNPCs.plugin.getNpcs().stream().anyMatch(humanEntity -> humanEntity.getBukkitEntity().getUniqueId() == event.getRightClicked().getUniqueId())) {
                    NPCTypes npcTypes = SereneNPCs.plugin.getNpcs()
                            .stream()
                            .filter(humanEntity -> humanEntity.getBukkitEntity().getUniqueId() == event.getRightClicked().getUniqueId())
                            .findAny().get().getNPCType();


                    ChatManager chatManager = new ChatManager();
                    ChatBuilder chatBuilder = chatManager.getChatBuilder(npcTypes);

                    ChatMaster chatMaster = new ChatMaster(player, chatBuilder);

                    PARRY_COOLDOWNS.add(player);
                    Bukkit.getScheduler().runTaskLater(SereneNPCs.plugin, () -> {
                        PARRY_COOLDOWNS.remove(player);
                    }, 20L);

                }
            }
        }
        else {
            if (!event.getPlayer().getOpenInventory().getType().equals(InventoryType.CHEST)) {
                if (SereneNPCs.plugin.getNpcs().stream().anyMatch(humanEntity -> humanEntity.getBukkitEntity().getUniqueId() == event.getRightClicked().getUniqueId())) {
                    SereneNPCs.plugin.getNpcs()
                            .stream()
                            .filter(humanEntity -> humanEntity.getBukkitEntity().getUniqueId() == event.getRightClicked().getUniqueId())
                            .findAny().get().openGUI(event.getPlayer());
                    event.setCancelled(true);

//            new MainGUI().openGUI(event.getPlayer());
                }
            }
        }

    }

    @EventHandler
    public void onKill(EntityDamageByEntityEvent event){
        if (event.getDamager() instanceof Player player){
            if (event.getEntity() instanceof LivingEntity livingEntity &&  livingEntity.getHealth() < event.getDamage()) {
                QuestGUI.decrementHuntKilLTracker(player, livingEntity);
            }
        }
    }


}

package com.sereneoasis.npc.random.guis.quests;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.sereneoasis.SereneNPCs;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import oshi.util.tuples.Pair;

import java.util.HashMap;
import java.util.UUID;

public class QuestGUI {

    private static final HashMap<UUID, Pair<EntityType, Integer>> HUNT_KILL_TRACKER = new HashMap<>();
    private static final HashMap<UUID, Pair<Location, Boolean>> EXPLORE_TRACKER = new HashMap<>();
    private ChestGui gui;
    private OutlinePane itemPane;

    public QuestGUI() {
        gui = new ChestGui(3, "Select amount");

        gui.setOnGlobalClick(event -> event.setCancelled(true));

        itemPane = new OutlinePane(0, 0, 9, 5);


        gui.addPane(itemPane);
    }

    public static void decrementHuntKilLTracker(Player killer, LivingEntity livingEntity) {
        if (HUNT_KILL_TRACKER.containsKey(killer.getUniqueId())) {
            if (HUNT_KILL_TRACKER.get(killer.getUniqueId()).getA().equals(livingEntity.getType())) {
                int newKillsLeft = HUNT_KILL_TRACKER.get(killer.getUniqueId()).getB() - 1;
                HUNT_KILL_TRACKER.put(killer.getUniqueId(), new Pair<>(livingEntity.getType(), newKillsLeft));
                Bukkit.broadcastMessage("You're making progress !");
            }
        }
    }

    public static void pollExploreTracker() {
        EXPLORE_TRACKER.forEach((uuid, locationBooleanPair) -> {
            if (locationBooleanPair.getB()) {
                return;
            }
            Player player = Bukkit.getPlayer(uuid);
            if (player == null || !player.isOnline()) {
                return;
            }
            Location location = locationBooleanPair.getA();
            if (player.getLocation().distanceSquared(location) < 100) {
                player.sendMessage("You have successfully arrived at the location!");
                EXPLORE_TRACKER.put(player.getUniqueId(), new Pair<>(location.clone(), true));
                return;
            }
            Bukkit.getScheduler().runTaskLater(SereneNPCs.plugin, () -> {
                player.sendMessage("You have an active quest, head to \n " +
                        "X: " + location.getX() + ", Y: " + location.getY() + ", Z: " + location.getZ());
                pollExploreTracker();
            }, 100);
        });
    }

    public void openGUI(Player player) {
        gui.show(player);
    }

    public void addAttainmentQuest(ItemStack required, ItemStack reward) {
        GuiItem rewardItem = new GuiItem(reward);
        itemPane.addItem(rewardItem);
        rewardItem.setAction(inventoryClickEvent -> {
            if (inventoryClickEvent.isRightClick()) {
                Inventory inv = inventoryClickEvent.getWhoClicked().getInventory();
                if (inv.containsAtLeast(required, 1)) {
                    ItemStack singularRequired = required.clone();
                    singularRequired.setAmount(1);
                    inv.remove(singularRequired);
                    if (inventoryClickEvent.getCurrentItem().getAmount() == 1) {
                        inventoryClickEvent.getWhoClicked().getInventory().addItem(reward);
                    }
                    inventoryClickEvent.getCurrentItem().setAmount(inventoryClickEvent.getCurrentItem().getAmount() - 1);

                }
            }
        });
    }

    public void addHuntQuest(ItemStack reward, EntityType entityType, int amount) {
        GuiItem rewardItem = new GuiItem(reward);
        itemPane.addItem(rewardItem);
        rewardItem.setAction(inventoryClickEvent -> {
            if (inventoryClickEvent.isRightClick()) {
                if (inventoryClickEvent.getWhoClicked() instanceof Player player) {
                    if (!HUNT_KILL_TRACKER.containsKey(player.getUniqueId())) {
                        Bukkit.broadcastMessage("You have accepted the quest!");

                        HUNT_KILL_TRACKER.put(player.getUniqueId(), new Pair<>(entityType, amount));
                    } else if (HUNT_KILL_TRACKER.get(player.getUniqueId()).getA().equals(entityType)) {
                        int newKillsLeft = HUNT_KILL_TRACKER.get(player.getUniqueId()).getB();
                        if (newKillsLeft <= 0) {
                            player.getInventory().addItem(reward);
                            inventoryClickEvent.getCurrentItem().setAmount(0);
                        }
                    }
                }
            }
        });
    }

    public void addExploreQuest(ItemStack reward, Location location) {
        GuiItem rewardItem = new GuiItem(reward);
        itemPane.addItem(rewardItem);
        rewardItem.setAction(inventoryClickEvent -> {
            if (inventoryClickEvent.isRightClick()) {
                if (inventoryClickEvent.getWhoClicked() instanceof Player player) {
                    if (!EXPLORE_TRACKER.containsKey(player.getUniqueId())) {
                        EXPLORE_TRACKER.put(player.getUniqueId(), new Pair<>(location.clone(), false));
                        pollExploreTracker();
                    } else {
                        if (EXPLORE_TRACKER.get(player.getUniqueId()).getB()) {
                            player.getInventory().addItem(reward);
                            inventoryClickEvent.getCurrentItem().setAmount(0);
                        }
                    }
                }
            }
        });
    }

    ;
}





























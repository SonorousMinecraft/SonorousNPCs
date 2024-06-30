package com.sereneoasis.npc.random.guis;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.sereneoasis.npc.random.guis.quests.QuestGUI;
import com.sereneoasis.npc.random.guis.shops.ShopGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class MainGUI {

    private ChestGui gui;

    public void openGUI(Player player) {
        gui.show(player);
    }

    public MainGUI(ShopGUI shopGUI, QuestGUI questGUI){

         gui = new ChestGui(3, "NPC");

        gui.setOnGlobalClick(event -> event.setCancelled(true));

        OutlinePane background = new OutlinePane(0, 0, 9, 3, Pane.Priority.LOWEST);
        background.addItem(new GuiItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));
        background.setRepeat(true);

        gui.addPane(background);

        OutlinePane navigationPane = new OutlinePane(3, 1, 3, 1);

        ItemStack shop = new ItemStack(Material.CHEST);
        ItemMeta shopMeta = shop.getItemMeta();
        shopMeta.setDisplayName("Shop");
        shop.setItemMeta(shopMeta);

        navigationPane.addItem(new GuiItem(shop, event -> {
            Player player = (Player) event.getWhoClicked();

            shopGUI.openGUI(player);
        }));

        ItemStack quest = new ItemStack(Material.KNOWLEDGE_BOOK);
        ItemMeta beaconMeta = quest.getItemMeta();
        beaconMeta.setDisplayName("Quests");
        beaconMeta.setLore(new ArrayList<>());
        quest.setItemMeta(beaconMeta);

        navigationPane.addItem(new GuiItem(quest, event -> {
            Player player = (Player) event.getWhoClicked();

//            questGUI.addAttainmentQuest(new ItemStack(Material.DIRT), ItemStacks.TRAINING_SWORD.getItemStack());
//            questGUI.addHuntQuest(ItemStacks.BASIC_SOLDIER_SWORD.getItemStack(), EntityType.ZOMBIE, 5);
//            questGUI.addExploreQuest(ItemStacks.BESERKER_AXE.getItemStack(), player.getLocation().add(100,0,0));

            questGUI.openGUI(player);
        }));

        ItemStack bed = new ItemStack(Material.RED_BED);
        ItemMeta bedMeta = bed.getItemMeta();
        bedMeta.setDisplayName("Home");
        bed.setItemMeta(bedMeta);

        navigationPane.addItem(new GuiItem(bed, event -> {
            //navigate to home
        }));

        gui.addPane(navigationPane);
    }
}

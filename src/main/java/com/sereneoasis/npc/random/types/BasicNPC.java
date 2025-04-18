package com.sereneoasis.npc.random.types;

import com.mojang.authlib.GameProfile;
import com.sereneoasis.entity.SereneHumanEntity;
import com.sereneoasis.npc.random.guis.MainGUI;
import com.sereneoasis.npc.random.guis.quests.QuestGUI;
import com.sereneoasis.npc.random.guis.shops.ShopGUI;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import org.bukkit.entity.Player;

/***
 * Attaches GUI system to NPCs
 */
public abstract class BasicNPC extends SereneHumanEntity implements GuiBuilder {

    private final ShopGUI shopGUI;
    private final QuestGUI questGUI;

    private final MainGUI mainGUI;

    public BasicNPC(MinecraftServer server, ServerLevel world, GameProfile profile, ClientInformation clientOptions) {
        super(server, world, profile, clientOptions);
        shopGUI = new ShopGUI();
        shopGUI.populateShop(getShopItems());
        questGUI = new QuestGUI();

        getExploreQuests().forEach((itemStack, location) -> {
            questGUI.addExploreQuest(itemStack, location);
        });

        getHuntQuests().forEach((itemStack, entityTypeIntegerPair) -> {
            questGUI.addHuntQuest(itemStack, entityTypeIntegerPair.getA(), entityTypeIntegerPair.getB());
        });

        getAttainmentQuests().forEach((itemStack, itemStack2) -> {
            questGUI.addAttainmentQuest(itemStack, itemStack2);
        });

        mainGUI = new MainGUI(shopGUI, questGUI);

    }

    public void openGUI(Player player) {
        mainGUI.openGUI(player);
    }

    public abstract NPCTypes getNPCType();
}

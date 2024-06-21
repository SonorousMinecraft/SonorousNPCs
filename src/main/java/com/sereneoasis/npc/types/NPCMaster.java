package com.sereneoasis.npc.types;

import com.mojang.authlib.GameProfile;
import com.sereneoasis.entity.HumanEntity;
import com.sereneoasis.npc.guis.MainGUI;
import com.sereneoasis.npc.guis.quests.QuestGUI;
import com.sereneoasis.npc.guis.shops.ShopGUI;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import org.bukkit.entity.Player;

public abstract class NPCMaster extends HumanEntity implements GuiBuilder {

    private ShopGUI shopGUI;
    private QuestGUI questGUI;

    private MainGUI mainGUI;
    public NPCMaster(MinecraftServer server, ServerLevel world, GameProfile profile, ClientInformation clientOptions) {
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

    public void openGUI(Player player){
        mainGUI.openGUI(player);
    }

    public abstract NPCTypes getNPCType();
}

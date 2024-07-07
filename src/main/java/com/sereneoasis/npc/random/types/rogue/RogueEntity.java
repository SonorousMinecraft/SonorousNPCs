package com.sereneoasis.npc.random.types.rogue;

import com.mojang.authlib.GameProfile;
import com.sereneoasis.items.ItemStacks;
import com.sereneoasis.npc.random.types.NPCBehaviourUtils;
import com.sereneoasis.npc.random.types.BasicNPC;
import com.sereneoasis.npc.random.types.NPCTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import oshi.util.tuples.Pair;

import java.util.HashMap;
import java.util.List;

public class RogueEntity extends BasicNPC {


    public RogueEntity(MinecraftServer server, ServerLevel world, GameProfile profile, ClientInformation clientOptions) {
        super(server, world, profile, clientOptions);

        this.setItemSlot(EquipmentSlot.HEAD, net.minecraft.world.item.ItemStack.fromBukkitCopy(new org.bukkit.inventory.ItemStack(Material.LEATHER_HELMET)));
        this.setItemSlot(EquipmentSlot.CHEST, net.minecraft.world.item.ItemStack.fromBukkitCopy(new org.bukkit.inventory.ItemStack(Material.CHAINMAIL_CHESTPLATE)));
        this.setItemSlot(EquipmentSlot.LEGS, net.minecraft.world.item.ItemStack.fromBukkitCopy(new org.bukkit.inventory.ItemStack(Material.IRON_LEGGINGS)));
        this.setItemSlot(EquipmentSlot.FEET, net.minecraft.world.item.ItemStack.fromBukkitCopy(new org.bukkit.inventory.ItemStack(Material.GOLDEN_BOOTS)));
    }

    @Override
    public NPCTypes getNPCType() {
        return NPCTypes.ROGUE;
    }

    @Override
    public void tick() {
        super.tick();

        NPCBehaviourUtils.normalBehaviour(this, masterGoalSelector, inventoryTracker, targetSelector);

    }


    @Override
    public HashMap<ItemStack, ItemStack> getAttainmentQuests() {
        HashMap<ItemStack, ItemStack> requirementRewardMap = new HashMap<>();

        return requirementRewardMap;
    }

    @Override
    public HashMap<ItemStack, Pair<EntityType, Integer>> getHuntQuests() {
        HashMap<ItemStack, Pair<EntityType, Integer>> rewardHuntAmountMap = new HashMap<>();

        return rewardHuntAmountMap;
    }

    @Override
    public HashMap<ItemStack, Location> getExploreQuests() {
        HashMap<ItemStack, Location> rewardLocationMap = new HashMap<>();

        return rewardLocationMap;
    }

    @Override
    public List<ItemStacks> getShopItems() {
        List<ItemStacks> shopArrayList = List.of(ItemStacks.WORN_ROGUE_RAGS, ItemStacks.STOLEN_INGOTS, ItemStacks.MYSTERIOUS_PEARLS);
        return shopArrayList;
    }
}

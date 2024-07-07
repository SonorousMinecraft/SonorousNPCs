package com.sereneoasis.npc.random.types.warrior;

import com.mojang.authlib.GameProfile;
import com.sereneoasis.items.ItemStacks;
import com.sereneoasis.npc.random.types.NPCBehaviourUtils;
import com.sereneoasis.npc.random.types.NPCMaster;
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

public class WarriorEntity extends NPCMaster {


    public WarriorEntity(MinecraftServer server, ServerLevel world, GameProfile profile, ClientInformation clientOptions) {
        super(server, world, profile, clientOptions);

        this.setItemSlot(EquipmentSlot.HEAD, net.minecraft.world.item.ItemStack.fromBukkitCopy(new org.bukkit.inventory.ItemStack(Material.IRON_HELMET)));
        this.setItemSlot(EquipmentSlot.CHEST, net.minecraft.world.item.ItemStack.fromBukkitCopy(new org.bukkit.inventory.ItemStack(Material.IRON_CHESTPLATE)));
        this.setItemSlot(EquipmentSlot.LEGS, net.minecraft.world.item.ItemStack.fromBukkitCopy(new org.bukkit.inventory.ItemStack(Material.IRON_LEGGINGS)));
        this.setItemSlot(EquipmentSlot.FEET, net.minecraft.world.item.ItemStack.fromBukkitCopy(new org.bukkit.inventory.ItemStack(Material.IRON_BOOTS)));
    }

    @Override
    public NPCTypes getNPCType() {
        return NPCTypes.WARRIOR;
    }

    @Override
    public void tick() {
        super.tick();

        NPCBehaviourUtils.normalBehaviour(this, masterGoalSelector, inventoryTracker, targetSelector);

    }


    @Override
    public HashMap<ItemStack, ItemStack> getAttainmentQuests() {
        HashMap<ItemStack, ItemStack> requirementRewardMap = new HashMap<>();
        requirementRewardMap.put(new ItemStack(Material.OAK_LOG), ItemStacks.TRAINING_SWORD.getItemStack());
        requirementRewardMap.put(new ItemStack(Material.COBBLESTONE), ItemStacks.BASIC_SOLDIER_SWORD.getItemStack());
        requirementRewardMap.put(new ItemStack(Material.IRON_INGOT), ItemStacks.BESERKER_AXE.getItemStack());
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
        List<ItemStacks> shopArrayList = List.of(ItemStacks.TRAINING_SWORD, ItemStacks.BASIC_SOLDIER_SWORD, ItemStacks.BESERKER_AXE);
        return shopArrayList;
    }
}

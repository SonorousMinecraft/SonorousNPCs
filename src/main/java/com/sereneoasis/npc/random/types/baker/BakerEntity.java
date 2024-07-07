package com.sereneoasis.npc.random.types.baker;

import com.mojang.authlib.GameProfile;
import com.sereneoasis.entity.AI.goal.complex.combat.KillTargetEntity;
import com.sereneoasis.items.ItemStacks;
import com.sereneoasis.npc.random.types.NPCBehaviourUtils;
import com.sereneoasis.npc.random.types.NPCMaster;
import com.sereneoasis.npc.random.types.NPCTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import oshi.util.tuples.Pair;

import java.util.HashMap;
import java.util.List;

public class BakerEntity extends NPCMaster {


    public BakerEntity(MinecraftServer server, ServerLevel world, GameProfile profile, ClientInformation clientOptions) {
        super(server, world, profile, clientOptions);

        this.setItemSlot(EquipmentSlot.FEET, net.minecraft.world.item.ItemStack.fromBukkitCopy(new org.bukkit.inventory.ItemStack(Material.LEATHER_BOOTS)));
    }

    @Override
    public NPCTypes getNPCType() {
        return NPCTypes.BAKER;
    }

    @Override
    public void tick() {
        super.tick();

        NPCBehaviourUtils.normalBehaviour(this, masterGoalSelector, inventoryTracker, targetSelector);

    }


    @Override
    public HashMap<ItemStack, ItemStack> getAttainmentQuests() {
        HashMap<ItemStack, ItemStack> requirementRewardMap = new HashMap<>();
        requirementRewardMap.put(new ItemStack(Material.WHEAT, 32), ItemStacks.BAKERS_LOAVES.getItemStack());
        requirementRewardMap.put(new ItemStack(Material.COCOA_BEANS, 8), ItemStacks.DOUBLE_CHOCOLATE_COOKIE.getItemStack());
        requirementRewardMap.put(new ItemStack(Material.SUGAR, 64), ItemStacks.RED_VELVET_CAKE.getItemStack());
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
        List<ItemStacks> shopArrayList = List.of(ItemStacks.BAKERS_LOAVES, ItemStacks.DOUBLE_CHOCOLATE_COOKIE, ItemStacks.RED_VELVET_CAKE);
        return shopArrayList;
    }
}

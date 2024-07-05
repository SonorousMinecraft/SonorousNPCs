package com.sereneoasis.npc.random.types.butcher;

import com.mojang.authlib.GameProfile;
import com.sereneoasis.entity.AI.goal.complex.combat.KillTargetEntity;
import com.sereneoasis.items.ItemCategory;
import com.sereneoasis.items.ItemStacks;
import com.sereneoasis.npc.random.types.NPCBehaviourUtils;
import com.sereneoasis.npc.random.types.NPCMaster;
import com.sereneoasis.npc.random.types.NPCTypes;
import com.sereneoasis.utils.Vec3Utils;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ButcherEntity extends NPCMaster {


    public ButcherEntity(MinecraftServer server, ServerLevel world, GameProfile profile, ClientInformation clientOptions) {
        super(server, world, profile, clientOptions);

        this.setItemSlot(EquipmentSlot.FEET, net.minecraft.world.item.ItemStack.fromBukkitCopy(new org.bukkit.inventory.ItemStack(Material.LEATHER_BOOTS)));
    }

    @Override
    public NPCTypes getNPCType() {
        return NPCTypes.BUTCHER;
    }
    @Override
    public void tick() {
        super.tick();

        NPCBehaviourUtils.normalBehaviour(this, masterGoalSelector, inventoryTracker, targetSelector);

    }


    @Override
    public HashMap<ItemStack, ItemStack> getAttainmentQuests() {
        HashMap<ItemStack, ItemStack> requirementRewardMap = new HashMap<>();
        requirementRewardMap.put(new ItemStack(Material.CHICKEN), ItemStacks.SLOW_ROASTED_CHICKEN.getItemStack());
        requirementRewardMap.put(new ItemStack(Material.BEEF), ItemStacks.SIRLOIN_STEAK.getItemStack());
        requirementRewardMap.put(new ItemStack(Material.PORKCHOP), ItemStacks.PAN_SEARED_PORK_CHOPS.getItemStack());
        requirementRewardMap.put(new ItemStack(Material.MUTTON), ItemStacks.LAMB_CHOPS.getItemStack());
        return requirementRewardMap;
    }

    @Override
    public HashMap<ItemStack, Pair<EntityType, Integer>> getHuntQuests() {
        HashMap<ItemStack, Pair<EntityType, Integer>> rewardHuntAmountMap = new HashMap<>();
        rewardHuntAmountMap.put(ItemStacks.SLOW_ROASTED_CHICKEN.getItemStack(), new Pair<>(EntityType.CHICKEN, 5));
        rewardHuntAmountMap.put(ItemStacks.SIRLOIN_STEAK.getItemStack(), new Pair<>(EntityType.COW, 5));
        rewardHuntAmountMap.put(ItemStacks.PAN_SEARED_PORK_CHOPS.getItemStack(), new Pair<>(EntityType.PIG, 5));
        rewardHuntAmountMap.put(ItemStacks.LAMB_CHOPS.getItemStack(), new Pair<>(EntityType.SHEEP, 5));
        return rewardHuntAmountMap;
    }

    @Override
    public HashMap<ItemStack, Location> getExploreQuests() {
        HashMap<ItemStack, Location> rewardLocationMap = new HashMap<>();

        return rewardLocationMap;
    }

    @Override
    public List<ItemStacks> getShopItems() {
        List<ItemStacks> shopArrayList = Arrays.stream(ItemStacks.values())
                .filter(itemStacks -> itemStacks.getCategory() == ItemCategory.MEAT)
                .collect(Collectors.toList());
        return shopArrayList;
    }
}

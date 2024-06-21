package com.sereneoasis.npc.types.mage;

import com.mojang.authlib.GameProfile;
import com.sereneoasis.entity.AI.goal.complex.combat.KillTargetEntity;
import com.sereneoasis.items.ItemStacks;
import com.sereneoasis.npc.types.NPCMaster;
import com.sereneoasis.npc.types.NPCTypes;
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

import java.util.HashMap;
import java.util.List;

public class MageEntity extends NPCMaster {


    public MageEntity(MinecraftServer server, ServerLevel world, GameProfile profile, ClientInformation clientOptions) {
        super(server, world, profile, clientOptions);

        this.setItemSlot(EquipmentSlot.HEAD, net.minecraft.world.item.ItemStack.fromBukkitCopy(new org.bukkit.inventory.ItemStack(Material.GOLDEN_HELMET)));
        this.setItemSlot(EquipmentSlot.CHEST, net.minecraft.world.item.ItemStack.fromBukkitCopy(new org.bukkit.inventory.ItemStack(Material.GOLDEN_CHESTPLATE)));
        this.setItemSlot(EquipmentSlot.LEGS, net.minecraft.world.item.ItemStack.fromBukkitCopy(new org.bukkit.inventory.ItemStack(Material.GOLDEN_LEGGINGS)));
        this.setItemSlot(EquipmentSlot.FEET, net.minecraft.world.item.ItemStack.fromBukkitCopy(new org.bukkit.inventory.ItemStack(Material.GOLDEN_BOOTS)));
    }

    @Override
    public NPCTypes getNPCType() {
        return NPCTypes.MAGE;
    }

    @Override
    public void tick() {
        super.tick();

        if (!masterGoalSelector.doingGoal("kill hostile entity")) {
            if (targetSelector.retrieveTopHostile() instanceof LivingEntity hostile && (!Vec3Utils.isObstructed(this.getPosition(0), hostile.getPosition(0), this.level()))) {
                masterGoalSelector.addMasterGoal(new KillTargetEntity("kill hostile entity", this, hostile));
            } else {
//                if (!masterGoalSelector.doingGoal("roam")) {
//                    masterGoalSelector.addMasterGoal(new RandomExploration("roam", this, null));
//                }
                if (!inventoryTracker.hasEnoughFood()) {
                    if (!masterGoalSelector.doingGoal("kill food entity")) {
                        if (targetSelector.retrieveTopPeaceful() instanceof LivingEntity peaceful) {
                            masterGoalSelector.addMasterGoal(new KillTargetEntity("kill food entity", this, peaceful));
                        }
                    }
                } else if (inventoryTracker.hasFood()) {
                    this.eat(this.level(), inventoryTracker.getMostAppropriateFood());
                }
            }
        }
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
        List<ItemStacks> shopArrayList = List.of();
        return shopArrayList;
    }
}

package com.sereneoasis.npc.random.types.assassin;

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

public class AssassinEntity extends BasicNPC {


    public AssassinEntity(MinecraftServer server, ServerLevel world, GameProfile profile, ClientInformation clientOptions) {
        super(server, world, profile, clientOptions);

        this.setItemSlot(EquipmentSlot.HEAD, net.minecraft.world.item.ItemStack.fromBukkitCopy(new org.bukkit.inventory.ItemStack(Material.CHAINMAIL_HELMET)));
        this.setItemSlot(EquipmentSlot.FEET, net.minecraft.world.item.ItemStack.fromBukkitCopy(new org.bukkit.inventory.ItemStack(Material.CHAINMAIL_BOOTS)));
    }

    @Override
    public NPCTypes getNPCType() {
        return NPCTypes.ASSASSIN;
    }

    @Override
    public void tick() {
        super.tick();

        NPCBehaviourUtils.normalBehaviour(this, masterGoalSelector, inventoryTracker, targetSelector);
    }


    @Override
    public HashMap<ItemStack, ItemStack> getAttainmentQuests() {
        HashMap<ItemStack, ItemStack> requirementRewardMap = new HashMap<>();
//        requirementRewardMap.
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
        List<ItemStacks> shopArrayList = List.of(ItemStacks.MURDERERS_DAGGER, ItemStacks.SILENT_SNIPER, ItemStacks.POISONED_ARROW);
        return shopArrayList;
    }
}

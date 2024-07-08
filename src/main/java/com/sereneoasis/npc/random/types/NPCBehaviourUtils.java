package com.sereneoasis.npc.random.types;

import com.sereneoasis.entity.AI.goal.MasterGoalSelector;
import com.sereneoasis.entity.AI.goal.complex.combat.KillTargetEntity;
import com.sereneoasis.entity.AI.goal.complex.movement.RandomExploration;
import com.sereneoasis.entity.AI.inventory.InventoryTracker;
import com.sereneoasis.entity.AI.target.TargetSelector;
import com.sereneoasis.utils.Vec3Utils;
import net.minecraft.world.entity.LivingEntity;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/***
 * A utility class to handle logic behind how an NPC chooses to do what it does
 */
public class NPCBehaviourUtils {

    private static final Set<BasicNPC> lookedForHostiles = new HashSet<>();

    private static final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();


    public static void normalBehaviour(BasicNPC basicNpc, MasterGoalSelector masterGoalSelector, InventoryTracker inventoryTracker, TargetSelector targetSelector) {
        if (!masterGoalSelector.doingGoal("kill hostile entity")) {
            if (!lookedForHostiles.contains(basicNpc) && targetSelector.retrieveTopHostile() instanceof LivingEntity hostile && (!Vec3Utils.isClearForMovementBetween(basicNpc, basicNpc.getEyePosition(), hostile.getEyePosition(), false))) {
                masterGoalSelector.addMasterGoal(new KillTargetEntity("kill hostile entity", basicNpc, hostile));
            } else {
                if (!lookedForHostiles.contains(basicNpc)) {
                    lookedForHostiles.add(basicNpc);


                    executorService.schedule(() -> lookedForHostiles.remove(basicNpc), 1, TimeUnit.SECONDS);

                }
//                if (!masterGoalSelector.doingGoal("roam")) {
//                    masterGoalSelector.addMasterGoal(new RandomExploration("roam", this, null));
//                }
                if (!inventoryTracker.hasEnoughFood()) {
                    if (!masterGoalSelector.doingGoal("kill food entity")) {
                        if (targetSelector.retrieveTopPeaceful() instanceof LivingEntity peaceful) {
                            masterGoalSelector.addMasterGoal(new KillTargetEntity("kill food entity", basicNpc, peaceful));
                        }
                    }
                } else if (inventoryTracker.hasFood()) {
                    basicNpc.eat(basicNpc.level(), inventoryTracker.getMostAppropriateFood());
                }
            }
        }
        if (!masterGoalSelector.doingGoal("roam")) {
            masterGoalSelector.addMasterGoal(new RandomExploration("roam", basicNpc, null));
        }
    }
}

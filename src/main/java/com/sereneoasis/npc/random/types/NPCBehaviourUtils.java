package com.sereneoasis.npc.random.types;

import com.sereneoasis.SereneNPCs;
import com.sereneoasis.entity.AI.goal.MasterGoalSelector;
import com.sereneoasis.entity.AI.goal.complex.combat.KillTargetEntity;
import com.sereneoasis.entity.AI.goal.complex.movement.RandomExploration;
import com.sereneoasis.entity.AI.inventory.InventoryTracker;
import com.sereneoasis.entity.AI.target.TargetSelector;
import com.sereneoasis.utils.Vec3Utils;
import net.minecraft.world.entity.LivingEntity;
import org.bukkit.Bukkit;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NPCBehaviourUtils {

    private static final Set<NPCMaster> lookedForHostiles = new HashSet<>();

    private static final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();


    public static void normalBehaviour(NPCMaster npcMaster, MasterGoalSelector masterGoalSelector, InventoryTracker inventoryTracker, TargetSelector targetSelector){
        if (!masterGoalSelector.doingGoal("kill hostile entity") ) {
            if (!lookedForHostiles.contains(npcMaster) && targetSelector.retrieveTopHostile() instanceof LivingEntity hostile && (!Vec3Utils.isClearForMovementBetween(npcMaster,npcMaster.getEyePosition(), hostile.getEyePosition(), false))) {
                masterGoalSelector.addMasterGoal(new KillTargetEntity("kill hostile entity", npcMaster, hostile));
            } else {
                if (!lookedForHostiles.contains(npcMaster)) {
                    lookedForHostiles.add(npcMaster);


                    executorService.schedule(() -> lookedForHostiles.remove(npcMaster), 1, TimeUnit.SECONDS);

                }
//                if (!masterGoalSelector.doingGoal("roam")) {
//                    masterGoalSelector.addMasterGoal(new RandomExploration("roam", this, null));
//                }
                if (!inventoryTracker.hasEnoughFood()) {
                    if (!masterGoalSelector.doingGoal("kill food entity")) {
                        if (targetSelector.retrieveTopPeaceful() instanceof LivingEntity peaceful) {
                            masterGoalSelector.addMasterGoal(new KillTargetEntity("kill food entity", npcMaster, peaceful));
                        }
                    }
                } else if (inventoryTracker.hasFood()) {
                    npcMaster.eat(npcMaster.level(), inventoryTracker.getMostAppropriateFood());
                }
            }
        }
        if (!masterGoalSelector.doingGoal("roam")){
            masterGoalSelector.addMasterGoal(new RandomExploration("roam", npcMaster, null ));
        }
    }
}

package com.sereneoasis.npc.random.types;

import com.sereneoasis.entity.AI.goal.MasterGoalSelector;
import com.sereneoasis.entity.AI.goal.complex.combat.KillTargetEntity;
import com.sereneoasis.entity.AI.goal.complex.movement.RandomExploration;
import com.sereneoasis.entity.AI.inventory.InventoryTracker;
import com.sereneoasis.entity.AI.target.TargetSelector;
import com.sereneoasis.utils.Vec3Utils;
import net.minecraft.world.entity.LivingEntity;

public class NPCBehaviourUtils {

    public static void normalBehaviour(NPCMaster npcMaster, MasterGoalSelector masterGoalSelector, InventoryTracker inventoryTracker, TargetSelector targetSelector){
        if (!masterGoalSelector.doingGoal("kill hostile entity") ) {
            if (targetSelector.retrieveTopHostile() instanceof LivingEntity hostile && (!Vec3Utils.isObstructed(npcMaster.getPosition(0), hostile.getPosition(0), npcMaster.level()))) {
                masterGoalSelector.addMasterGoal(new KillTargetEntity("kill hostile entity", npcMaster, hostile));
            } else {
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

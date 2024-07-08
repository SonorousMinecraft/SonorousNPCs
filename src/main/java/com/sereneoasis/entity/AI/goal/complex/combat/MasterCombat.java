package com.sereneoasis.entity.AI.goal.complex.combat;

import com.sereneoasis.entity.AI.goal.complex.MasterGoal;
import com.sereneoasis.entity.SereneHumanEntity;
import net.minecraft.world.entity.LivingEntity;

public abstract class MasterCombat extends MasterGoal {

    protected LivingEntity entity;

    public MasterCombat(String name, SereneHumanEntity npc) {
        super(name, npc);
    }

    @Override
    public void tick() {
        if (isFinished()) {
            return;
        }
        super.tick();
        if (!entity.isAlive()) {
            this.finished = true;
        }

    }
}

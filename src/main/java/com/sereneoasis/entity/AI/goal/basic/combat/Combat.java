package com.sereneoasis.entity.AI.goal.basic.combat;

import com.sereneoasis.entity.AI.goal.basic.BasicGoal;
import com.sereneoasis.entity.AI.goal.interfaces.EntityInteraction;
import com.sereneoasis.entity.SereneHumanEntity;
import net.minecraft.world.entity.LivingEntity;

public abstract class Combat extends BasicGoal implements EntityInteraction {

    protected LivingEntity entity;

    public Combat(String name, SereneHumanEntity npc, int priority, LivingEntity entity) {
        super(name, npc, priority);

        this.entity = entity;
    }

    @Override
    public LivingEntity getEntity() {
        return entity;
    }


}

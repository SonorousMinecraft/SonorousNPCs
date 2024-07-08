package com.sereneoasis.entity.AI.goal.basic.movement;

import com.sereneoasis.entity.AI.goal.basic.BasicGoal;
import com.sereneoasis.entity.AI.navigation.PathNavigation;
import com.sereneoasis.entity.AI.pathfinding.Navigation;
import com.sereneoasis.entity.SereneHumanEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;

public abstract class Movement extends BasicGoal {

    private Vec3 goalPos;

    private double requiredDistance;

    private Navigation navigation;

    public Movement(String name, SereneHumanEntity npc, int priority, Vec3 goalPos, double requiredDistance) {
        super(name, npc, priority);

        this.goalPos = goalPos;
        this.requiredDistance = requiredDistance;

        navigation = npc.getNavigation();
        navigation.navigateToPos(goalPos);

    }

    public Vec3 getGoalPos() {
        return goalPos;
    }

    public void setGoalPos(Vec3 goalPos) {
        this.goalPos = goalPos;
    }

    public double getDistance() {
        return goalPos.distanceTo(npc.getOnPos().getCenter());
    }

    private boolean isNavigating;
    @Override
    public void tick() {

//            if (npc.getNavigation().isStuck() ) {
//                npc.getNavigation().recomputePath();
//            }

//        if (npc.getNavigation().isStuck()){
//            npc.getNavigation().moveTo(goalPos.x, goalPos.y, goalPos.z, 10);
//
//        }
        if (goalPos != null) {
            if (getDistance() > requiredDistance) {

//                npc.getNavigation().moveTo(goalPos.x, goalPos.y, goalPos.z, 10);
                //  npc.getNavigation().createPath(BlockPos.containing(goalPos), 1000);
            } else {
                finished = true;
            }
        }
    }
}

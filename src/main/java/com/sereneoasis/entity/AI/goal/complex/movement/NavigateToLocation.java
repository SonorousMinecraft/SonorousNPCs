package com.sereneoasis.entity.AI.goal.complex.movement;

import com.sereneoasis.entity.AI.goal.basic.look.PeriodicallyRotate;
import com.sereneoasis.entity.AI.goal.basic.movement.MoveForward;
import com.sereneoasis.entity.HumanEntity;
import net.minecraft.core.BlockPos;
import org.bukkit.Bukkit;

import java.util.function.Predicate;

public class NavigateToLocation extends MasterMovement{

    private MoveForward moveForward;
    private PeriodicallyRotate periodicallyRotate;

    public NavigateToLocation(String name, HumanEntity npc, Predicate<BlockPos> condition) {
        super(name, npc, condition);

        this.moveForward = new MoveForward("move", npc, 1, 3);
        movementGoalSelector.addGoal(moveForward);

        this.periodicallyRotate = new PeriodicallyRotate("rotate", npc, 1, 40, 60);
        //lookGoalSelector.addGoal(periodicallyRotate);
    }

    @Override
    public void tick() {
        super.tick();
        if (moveForward.isBugged()) {
            //Bukkit.broadcastMessage("can't go anywhere");

        } else {
            if (moveForward.isStuck()) {
                periodicallyRotate.prematureRotate(180);
                moveForward.tick();
            }
            if (moveForward.isFinished()){
                Bukkit.broadcastMessage("finished");
                this.setFinished(true);
            }
        }
    }
}

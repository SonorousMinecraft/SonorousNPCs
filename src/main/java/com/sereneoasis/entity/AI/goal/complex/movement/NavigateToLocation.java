package com.sereneoasis.entity.AI.goal.complex.movement;

import com.sereneoasis.entity.AI.goal.basic.look.PeriodicallyRotate;
import com.sereneoasis.entity.AI.goal.basic.movement.MoveToBlock;
import com.sereneoasis.entity.SereneHumanEntity;
import net.minecraft.core.BlockPos;
import org.bukkit.Bukkit;

import java.util.function.Predicate;

public class NavigateToLocation extends MasterMovement {

    private MoveToBlock moveToBlock;
    private PeriodicallyRotate periodicallyRotate;

    public NavigateToLocation(String name, SereneHumanEntity npc, Predicate<BlockPos> condition, BlockPos blockPos) {
        super(name, npc, condition);

        this.moveToBlock = new MoveToBlock("move", npc, blockPos);
        movementGoalSelector.addGoal(moveToBlock);

//        this.periodicallyRotate = new PeriodicallyRotate("rotate", npc, 1, 40, 60);
//        lookGoalSelector.addGoal(periodicallyRotate);
    }

    @Override
    public void tick() {
        super.tick();
        if (moveToBlock.isFinished()) {
            Bukkit.broadcastMessage("finished");
            this.setFinished(true);
        }
    }
}


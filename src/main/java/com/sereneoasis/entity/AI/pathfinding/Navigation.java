package com.sereneoasis.entity.AI.pathfinding;

import com.sereneoasis.entity.AI.control.MoveControl;
import com.sereneoasis.entity.SereneHumanEntity;
import net.minecraft.core.BlockPos;

import net.minecraft.core.Direction;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;

import java.util.Comparator;
import java.util.List;

public class Navigation {

    private final SereneHumanEntity humanEntity;

    private final MoveControl moveControl;

    private Vec3 goalPos;

    private Vec3 newPos;

    private boolean isStuck = false;
    

    public Navigation(SereneHumanEntity humanEntity){
        this.humanEntity = humanEntity;
        this.moveControl = humanEntity.getMoveControl();
    }

    private static final List<Direction> directions = List.of(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);

    public void navigateToPos(Vec3 pos){
        this.goalPos = pos;
    }

    private boolean isSolid(Vec3 pos){
        return !humanEntity.level().getBlockState(BlockPos.containing(pos)).isAir();
    }

    private boolean isAir(Vec3 pos){
        return humanEntity.level().getBlockState(BlockPos.containing(pos)).isAir();
    }


    private boolean isAcceptableDirection(Direction direction){
        Vec3 currentPos = humanEntity.getPosition(0);
        Vec3 newPos = currentPos.relative(direction, 1);
        return (  (isAir(newPos.relative(Direction.UP, 1)) && isSolid(newPos) ) || // forward
                (isAir(newPos) && isSolid(newPos.relative(Direction.DOWN, 1)) ) || // down
                (isAir(newPos.relative(Direction.UP, 2)) && isSolid(newPos.relative(Direction.UP, 1)) )); //up
    }

    public void tick(){
        if (!isStuck && goalPos != null) {
                directions.stream().filter(this::isAcceptableDirection)
                        .min(Comparator.comparingDouble(value -> humanEntity.getPosition(0).relative(value, 1).distanceToSqr(goalPos)))
                        .ifPresentOrElse(direction -> {
                            newPos = humanEntity.getPosition(0).relative(direction, 1);
                            moveControl.setWantedPosition(newPos.x, newPos.y, newPos.z, 10);
                        }, () -> {
                            isStuck = true;
                            Bukkit.broadcastMessage("Stuck");
                        });

        }
    }
}

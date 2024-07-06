package com.sereneoasis.utils;

import com.sereneoasis.entity.AI.navigation.PathNavigation;
import com.sereneoasis.entity.SereneHumanEntity;
import com.sereneoasis.npc.random.types.NPCMaster;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.bukkit.util.Vector;

public class Vec3Utils {

    public static boolean isBlockSolid(Position pos, Level level) {
        BlockState blockState = level.getBlockStateIfLoaded(BlockPos.containing(pos));
        if (blockState == null){
            return false;
        } else if (blockState.isSolid()) {
            return true;
        }
        return false;
    }

    public static boolean isTopBlock(Position pos, Level level){
        return !isBlockSolid( BlockPos.containing(pos).above().getCenter(), level) && isBlockSolid( BlockPos.containing(pos).below().getCenter(), level);
    }

    public static boolean isBlockSolid(BlockPos pos, Level level) {
        BlockState blockState = level.getBlockStateIfLoaded(pos);
        if (blockState == null){
            return false;
        } else if (blockState.isSolid()) {
            return true;
        }
        return false;
    }

    public static boolean isObstructed(Position pos1, Position pos2, Level level) {
        boolean obstructed = false;

        for (BlockPos bp :   BlockPos.betweenClosed(BlockPos.containing(pos1), BlockPos.containing(pos2))){
            if (isBlockSolid(bp, level)) {
                obstructed = true;
                break;
            }
        }
        return obstructed;
    }

    public static boolean isClearForMovementBetween(SereneHumanEntity entity, Vec3 startPos, Vec3 entityPos, boolean includeFluids) {
        Vec3 vec3 = new Vec3(entityPos.x, entityPos.y + (double)entity.getBbHeight() * 0.5, entityPos.z);
        return entity.level().clip(new ClipContext(startPos, vec3, ClipContext.Block.COLLIDER, includeFluids ? ClipContext.Fluid.ANY : ClipContext.Fluid.NONE, entity)).getType() == HitResult.Type.MISS;
    }
}

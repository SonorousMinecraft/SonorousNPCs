//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.sereneoasis.entity.AI.navigation;

import com.sereneoasis.entity.SereneHumanEntity;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.pathfinder.Target;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Iterator;

public class WalkNodeEvaluator extends NodeEvaluator {
    public static final double SPACE_BETWEEN_WALL_POSTS = 0.5;
    private static final double DEFAULT_MOB_JUMP_HEIGHT = 1.125;
    private final Long2ObjectMap<BlockPathTypes> pathTypesByPosCache = new Long2ObjectOpenHashMap();
    private final Object2BooleanMap<AABB> collisionCache = new Object2BooleanOpenHashMap();

    public WalkNodeEvaluator() {
    }

    private static boolean doesBlockHavePartialCollision(BlockPathTypes nodeType) {
        return nodeType == BlockPathTypes.FENCE || nodeType == BlockPathTypes.DOOR_WOOD_CLOSED || nodeType == BlockPathTypes.DOOR_IRON_CLOSED;
    }

    public static double getFloorLevel(BlockGetter world, BlockPos pos) {
        BlockPos blockPos = pos.below();
        VoxelShape voxelShape = world.getBlockState(blockPos).getCollisionShape(world, blockPos);
        return (double) blockPos.getY() + (voxelShape.isEmpty() ? 0.0 : voxelShape.max(Axis.Y));
    }

    public static BlockPathTypes getBlockPathTypeStatic(BlockGetter world, BlockPos.MutableBlockPos pos) {
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        BlockPathTypes blockPathTypes = getBlockPathTypeRaw(world, pos);
        if (blockPathTypes == BlockPathTypes.OPEN && j >= world.getMinBuildHeight() + 1) {
            BlockPathTypes blockPathTypes2 = getBlockPathTypeRaw(world, pos.set(i, j - 1, k));
            blockPathTypes = blockPathTypes2 != BlockPathTypes.WALKABLE && blockPathTypes2 != BlockPathTypes.OPEN && blockPathTypes2 != BlockPathTypes.WATER && blockPathTypes2 != BlockPathTypes.LAVA ? BlockPathTypes.WALKABLE : BlockPathTypes.OPEN;
            if (blockPathTypes2 == BlockPathTypes.DAMAGE_FIRE) {
                blockPathTypes = BlockPathTypes.DAMAGE_FIRE;
            }

            if (blockPathTypes2 == BlockPathTypes.DAMAGE_OTHER) {
                blockPathTypes = BlockPathTypes.DAMAGE_OTHER;
            }

            if (blockPathTypes2 == BlockPathTypes.STICKY_HONEY) {
                blockPathTypes = BlockPathTypes.STICKY_HONEY;
            }

            if (blockPathTypes2 == BlockPathTypes.POWDER_SNOW) {
                blockPathTypes = BlockPathTypes.DANGER_POWDER_SNOW;
            }

            if (blockPathTypes2 == BlockPathTypes.DAMAGE_CAUTIOUS) {
                blockPathTypes = BlockPathTypes.DAMAGE_CAUTIOUS;
            }
        }

        if (blockPathTypes == BlockPathTypes.WALKABLE) {
            blockPathTypes = checkNeighbourBlocks(world, pos.set(i, j, k), blockPathTypes);
        }

        return blockPathTypes;
    }

    public static BlockPathTypes checkNeighbourBlocks(BlockGetter world, BlockPos.MutableBlockPos pos, BlockPathTypes nodeType) {
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();

        for (int l = -1; l <= 1; ++l) {
            for (int m = -1; m <= 1; ++m) {
                for (int n = -1; n <= 1; ++n) {
                    if (l != 0 || n != 0) {
                        pos.set(i + l, j + m, k + n);
                        BlockState blockState = world.getBlockStateIfLoaded(pos);
                        if (blockState == null) {
                            return BlockPathTypes.BLOCKED;
                        }

                        if (blockState.is(Blocks.CACTUS) || blockState.is(Blocks.SWEET_BERRY_BUSH)) {
                            return BlockPathTypes.DANGER_OTHER;
                        }

                        if (isBurningBlock(blockState)) {
                            return BlockPathTypes.DANGER_FIRE;
                        }

                        if (blockState.getFluidState().is(FluidTags.WATER)) {
                            return BlockPathTypes.WATER_BORDER;
                        }

                        if (blockState.is(Blocks.WITHER_ROSE) || blockState.is(Blocks.POINTED_DRIPSTONE)) {
                            return BlockPathTypes.DAMAGE_CAUTIOUS;
                        }
                    }
                }
            }
        }

        return nodeType;
    }

    protected static BlockPathTypes getBlockPathTypeRaw(BlockGetter world, BlockPos pos) {
        BlockState blockState = world.getBlockStateIfLoaded(pos);
        if (blockState == null) {
            return BlockPathTypes.BLOCKED;
        } else {
            Block block = blockState.getBlock();
            if (blockState.isAir()) {
                return BlockPathTypes.OPEN;
            } else if (!blockState.is(BlockTags.TRAPDOORS) && !blockState.is(Blocks.LILY_PAD) && !blockState.is(Blocks.BIG_DRIPLEAF)) {
                if (blockState.is(Blocks.POWDER_SNOW)) {
                    return BlockPathTypes.POWDER_SNOW;
                } else if (!blockState.is(Blocks.CACTUS) && !blockState.is(Blocks.SWEET_BERRY_BUSH)) {
                    if (blockState.is(Blocks.HONEY_BLOCK)) {
                        return BlockPathTypes.STICKY_HONEY;
                    } else if (blockState.is(Blocks.COCOA)) {
                        return BlockPathTypes.COCOA;
                    } else if (!blockState.is(Blocks.WITHER_ROSE) && !blockState.is(Blocks.POINTED_DRIPSTONE)) {
                        FluidState fluidState = blockState.getFluidState();
                        if (fluidState.is(FluidTags.LAVA)) {
                            return BlockPathTypes.LAVA;
                        } else if (isBurningBlock(blockState)) {
                            return BlockPathTypes.DAMAGE_FIRE;
                        } else if (block instanceof DoorBlock) {
                            DoorBlock doorBlock = (DoorBlock) block;
                            if ((Boolean) blockState.getValue(DoorBlock.OPEN)) {
                                return BlockPathTypes.DOOR_OPEN;
                            } else {
                                return doorBlock.type().canOpenByHand() ? BlockPathTypes.DOOR_WOOD_CLOSED : BlockPathTypes.DOOR_IRON_CLOSED;
                            }
                        } else if (block instanceof BaseRailBlock) {
                            return BlockPathTypes.RAIL;
                        } else if (block instanceof LeavesBlock) {
                            return BlockPathTypes.LEAVES;
                        } else if (blockState.is(BlockTags.FENCES) || blockState.is(BlockTags.WALLS) || block instanceof FenceGateBlock && !(Boolean) blockState.getValue(FenceGateBlock.OPEN)) {
                            return BlockPathTypes.FENCE;
                        } else if (!blockState.isPathfindable(world, pos, PathComputationType.LAND)) {
                            return BlockPathTypes.BLOCKED;
                        } else {
                            return fluidState.is(FluidTags.WATER) ? BlockPathTypes.WATER : BlockPathTypes.OPEN;
                        }
                    } else {
                        return BlockPathTypes.DAMAGE_CAUTIOUS;
                    }
                } else {
                    return BlockPathTypes.DAMAGE_OTHER;
                }
            } else {
                return BlockPathTypes.TRAPDOOR;
            }
        }
    }

    public static boolean isBurningBlock(BlockState state) {
        return state.is(BlockTags.FIRE) || state.is(Blocks.LAVA) || state.is(Blocks.MAGMA_BLOCK) || CampfireBlock.isLitCampfire(state) || state.is(Blocks.LAVA_CAULDRON);
    }

    public void prepare(PathNavigationRegion cachedWorld, SereneHumanEntity entity) {
        super.prepare(cachedWorld, entity);
        entity.onPathfindingStart();
    }

    public void done() {
        this.mob.onPathfindingDone();
        this.pathTypesByPosCache.clear();
        this.collisionCache.clear();
        super.done();
    }

    public Node getStart() {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        int i = this.mob.getBlockY();
        BlockState blockState = this.level.getBlockState(mutableBlockPos.set(this.mob.getX(), (double) i, this.mob.getZ()));
        BlockPos blockPos;
        if (!this.mob.canStandOnFluid(blockState.getFluidState())) {
            if (this.canFloat() && this.mob.isInWater()) {
                while (true) {
                    if (!blockState.is(Blocks.WATER) && blockState.getFluidState() != Fluids.WATER.getSource(false)) {
                        --i;
                        break;
                    }

                    ++i;
                    blockState = this.level.getBlockState(mutableBlockPos.set(this.mob.getX(), (double) i, this.mob.getZ()));
                }
            } else if (this.mob.onGround()) {
                i = Mth.floor(this.mob.getY() + 0.5);
            } else {
                for (blockPos = this.mob.blockPosition(); (this.level.getBlockState(blockPos).isAir() || this.level.getBlockState(blockPos).isPathfindable(this.level, blockPos, PathComputationType.LAND)) && blockPos.getY() > this.mob.level().getMinBuildHeight(); blockPos = blockPos.below()) {
                }

                i = blockPos.above().getY();
            }
        } else {
            while (true) {
                if (!this.mob.canStandOnFluid(blockState.getFluidState())) {
                    --i;
                    break;
                }

                ++i;
                blockState = this.level.getBlockState(mutableBlockPos.set(this.mob.getX(), (double) i, this.mob.getZ()));
            }
        }

        blockPos = this.mob.blockPosition();
        if (!this.canStartAt(mutableBlockPos.set(blockPos.getX(), i, blockPos.getZ()))) {
            AABB aABB = this.mob.getBoundingBox();
            if (this.canStartAt(mutableBlockPos.set(aABB.minX, (double) i, aABB.minZ)) || this.canStartAt(mutableBlockPos.set(aABB.minX, (double) i, aABB.maxZ)) || this.canStartAt(mutableBlockPos.set(aABB.maxX, (double) i, aABB.minZ)) || this.canStartAt(mutableBlockPos.set(aABB.maxX, (double) i, aABB.maxZ))) {
                return this.getStartNode(mutableBlockPos);
            }
        }

        return this.getStartNode(new BlockPos(blockPos.getX(), i, blockPos.getZ()));
    }

    protected Node getStartNode(BlockPos pos) {
        Node node = this.getNode(pos);
        node.type = this.getBlockPathType(this.mob, node.asBlockPos());
        node.costMalus = this.mob.getPathfindingMalus(node.type);
        return node;
    }

    protected boolean canStartAt(BlockPos pos) {
        BlockPathTypes blockPathTypes = this.getBlockPathType(this.mob, pos);
        return blockPathTypes != BlockPathTypes.OPEN && this.mob.getPathfindingMalus(blockPathTypes) >= 0.0F;
    }

    public Target getGoal(double x, double y, double z) {
        return this.getTargetFromNode(this.getNode(Mth.floor(x), Mth.floor(y), Mth.floor(z)));
    }

    public int getNeighbors(Node[] successors, Node node) {
        int i = 0;
        int j = 0;
        BlockPathTypes blockPathTypes = this.getCachedBlockType(this.mob, node.x, node.y + 1, node.z);
        BlockPathTypes blockPathTypes2 = this.getCachedBlockType(this.mob, node.x, node.y, node.z);
        if (this.mob.getPathfindingMalus(blockPathTypes) >= 0.0F && blockPathTypes2 != BlockPathTypes.STICKY_HONEY) {
            j = Mth.floor(Math.max(1.0F, this.mob.maxUpStep()));
        }

        double d = this.getFloorLevel(new BlockPos(node.x, node.y, node.z));
        Node node2 = this.findAcceptedNode(node.x, node.y, node.z + 1, j, d, Direction.SOUTH, blockPathTypes2);
        if (this.isNeighborValid(node2, node)) {
            successors[i++] = node2;
        }

        Node node3 = this.findAcceptedNode(node.x - 1, node.y, node.z, j, d, Direction.WEST, blockPathTypes2);
        if (this.isNeighborValid(node3, node)) {
            successors[i++] = node3;
        }

        Node node4 = this.findAcceptedNode(node.x + 1, node.y, node.z, j, d, Direction.EAST, blockPathTypes2);
        if (this.isNeighborValid(node4, node)) {
            successors[i++] = node4;
        }

        Node node5 = this.findAcceptedNode(node.x, node.y, node.z - 1, j, d, Direction.NORTH, blockPathTypes2);
        if (this.isNeighborValid(node5, node)) {
            successors[i++] = node5;
        }

        Node node6 = this.findAcceptedNode(node.x - 1, node.y, node.z - 1, j, d, Direction.NORTH, blockPathTypes2);
        if (this.isDiagonalValid(node, node3, node5, node6)) {
            successors[i++] = node6;
        }

        Node node7 = this.findAcceptedNode(node.x + 1, node.y, node.z - 1, j, d, Direction.NORTH, blockPathTypes2);
        if (this.isDiagonalValid(node, node4, node5, node7)) {
            successors[i++] = node7;
        }

        Node node8 = this.findAcceptedNode(node.x - 1, node.y, node.z + 1, j, d, Direction.SOUTH, blockPathTypes2);
        if (this.isDiagonalValid(node, node3, node2, node8)) {
            successors[i++] = node8;
        }

        Node node9 = this.findAcceptedNode(node.x + 1, node.y, node.z + 1, j, d, Direction.SOUTH, blockPathTypes2);
        if (this.isDiagonalValid(node, node4, node2, node9)) {
            successors[i++] = node9;
        }

        return i;
    }

    protected boolean isNeighborValid(@Nullable Node node, Node successor1) {
        return node != null && !node.closed && (node.costMalus >= 0.0F || successor1.costMalus < 0.0F);
    }

    protected boolean isDiagonalValid(Node xNode, @Nullable Node zNode, @Nullable Node xDiagNode, @Nullable Node zDiagNode) {
        if (zDiagNode != null && xDiagNode != null && zNode != null) {
            if (zDiagNode.closed) {
                return false;
            } else if (xDiagNode.y <= xNode.y && zNode.y <= xNode.y) {
                if (zNode.type != BlockPathTypes.WALKABLE_DOOR && xDiagNode.type != BlockPathTypes.WALKABLE_DOOR && zDiagNode.type != BlockPathTypes.WALKABLE_DOOR) {
                    boolean bl = xDiagNode.type == BlockPathTypes.FENCE && zNode.type == BlockPathTypes.FENCE && (double) this.mob.getBbWidth() < 0.5;
                    return zDiagNode.costMalus >= 0.0F && (xDiagNode.y < xNode.y || xDiagNode.costMalus >= 0.0F || bl) && (zNode.y < xNode.y || zNode.costMalus >= 0.0F || bl);
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean canReachWithoutCollision(Node node) {
        AABB aABB = this.mob.getBoundingBox();
        Vec3 vec3 = new Vec3((double) node.x - this.mob.getX() + aABB.getXsize() / 2.0, (double) node.y - this.mob.getY() + aABB.getYsize() / 2.0, (double) node.z - this.mob.getZ() + aABB.getZsize() / 2.0);
        int i = Mth.ceil(vec3.length() / aABB.getSize());
        vec3 = vec3.scale((double) (1.0F / (float) i));

        for (int j = 1; j <= i; ++j) {
            aABB = aABB.move(vec3);
            if (this.hasCollisions(aABB)) {
                return false;
            }
        }

        return true;
    }

    protected double getFloorLevel(BlockPos pos) {
        return (this.canFloat() || this.isAmphibious()) && this.level.getFluidState(pos).is(FluidTags.WATER) ? (double) pos.getY() + 0.5 : getFloorLevel(this.level, pos);
    }

    protected boolean isAmphibious() {
        return false;
    }

    @Nullable
    protected Node findAcceptedNode(int x, int y, int z, int maxYStep, double prevFeetY, Direction direction, BlockPathTypes nodeType) {
        Node node = null;
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        double d = this.getFloorLevel(mutableBlockPos.set(x, y, z));
        if (d - prevFeetY > this.getHumanEntityJumpHeight()) {
            return null;
        } else {
            BlockPathTypes blockPathTypes = this.getCachedBlockType(this.mob, x, y, z);
            float f = this.mob.getPathfindingMalus(blockPathTypes);
            double e = (double) this.mob.getBbWidth() / 2.0;
            if (f >= 0.0F) {
                node = this.getNodeAndUpdateCostToMax(x, y, z, blockPathTypes, f);
            }

            if (doesBlockHavePartialCollision(nodeType) && node != null && node.costMalus >= 0.0F && !this.canReachWithoutCollision(node)) {
                node = null;
            }

            if (blockPathTypes == BlockPathTypes.WALKABLE || this.isAmphibious() && blockPathTypes == BlockPathTypes.WATER) {
                return node;
            } else {
                if ((node == null || node.costMalus < 0.0F) && maxYStep > 0 && (blockPathTypes != BlockPathTypes.FENCE || this.canWalkOverFences()) && blockPathTypes != BlockPathTypes.UNPASSABLE_RAIL && blockPathTypes != BlockPathTypes.TRAPDOOR && blockPathTypes != BlockPathTypes.POWDER_SNOW) {
                    node = this.findAcceptedNode(x, y + 1, z, maxYStep - 1, prevFeetY, direction, nodeType);
                    if (node != null && (node.type == BlockPathTypes.OPEN || node.type == BlockPathTypes.WALKABLE) && this.mob.getBbWidth() < 1.0F) {
                        double g = (double) (x - direction.getStepX()) + 0.5;
                        double h = (double) (z - direction.getStepZ()) + 0.5;
                        AABB aABB = new AABB(g - e, this.getFloorLevel(mutableBlockPos.set(g, (double) (y + 1), h)) + 0.001, h - e, g + e, (double) this.mob.getBbHeight() + this.getFloorLevel(mutableBlockPos.set((double) node.x, (double) node.y, (double) node.z)) - 0.002, h + e);
                        if (this.hasCollisions(aABB)) {
                            node = null;
                        }
                    }
                }

                if (!this.isAmphibious() && blockPathTypes == BlockPathTypes.WATER && !this.canFloat()) {
                    if (this.getCachedBlockType(this.mob, x, y - 1, z) != BlockPathTypes.WATER) {
                        return node;
                    }

                    while (y > this.mob.level().getMinBuildHeight()) {
                        --y;
                        blockPathTypes = this.getCachedBlockType(this.mob, x, y, z);
                        if (blockPathTypes != BlockPathTypes.WATER) {
                            return node;
                        }

                        node = this.getNodeAndUpdateCostToMax(x, y, z, blockPathTypes, this.mob.getPathfindingMalus(blockPathTypes));
                    }
                }

                if (blockPathTypes == BlockPathTypes.OPEN) {
                    int i = 0;
                    int j = y;

                    while (blockPathTypes == BlockPathTypes.OPEN) {
                        --y;
                        if (y < this.mob.level().getMinBuildHeight()) {
                            return this.getBlockedNode(x, j, z);
                        }

                        if (i++ >= this.mob.getMaxFallDistance()) {
                            return this.getBlockedNode(x, y, z);
                        }

                        blockPathTypes = this.getCachedBlockType(this.mob, x, y, z);
                        f = this.mob.getPathfindingMalus(blockPathTypes);
                        if (blockPathTypes != BlockPathTypes.OPEN && f >= 0.0F) {
                            node = this.getNodeAndUpdateCostToMax(x, y, z, blockPathTypes, f);
                            break;
                        }

                        if (f < 0.0F) {
                            return this.getBlockedNode(x, y, z);
                        }
                    }
                }

                if (doesBlockHavePartialCollision(blockPathTypes) && node == null) {
                    node = this.getNode(x, y, z);
                    node.closed = true;
                    node.type = blockPathTypes;
                    node.costMalus = blockPathTypes.getMalus();
                }

                return node;
            }
        }
    }

    private double getHumanEntityJumpHeight() {
        return Math.max(1.125, (double) this.mob.maxUpStep());
    }

    private Node getNodeAndUpdateCostToMax(int x, int y, int z, BlockPathTypes type, float penalty) {
        Node node = this.getNode(x, y, z);
        node.type = type;
        node.costMalus = Math.max(node.costMalus, penalty);
        return node;
    }

    private Node getBlockedNode(int x, int y, int z) {
        Node node = this.getNode(x, y, z);
        node.type = BlockPathTypes.BLOCKED;
        node.costMalus = -1.0F;
        return node;
    }

    private boolean hasCollisions(AABB box) {
        return this.collisionCache.computeIfAbsent(box, (box2) -> {
            return !this.level.noCollision(this.mob, box);
        });
    }

    public BlockPathTypes getBlockPathType(BlockGetter world, int x, int y, int z, SereneHumanEntity mob) {
        EnumSet<BlockPathTypes> enumSet = EnumSet.noneOf(BlockPathTypes.class);
        BlockPathTypes blockPathTypes = BlockPathTypes.BLOCKED;
        blockPathTypes = this.getBlockPathTypes(world, x, y, z, enumSet, blockPathTypes, mob.blockPosition());
        if (enumSet.contains(BlockPathTypes.FENCE)) {
            return BlockPathTypes.FENCE;
        } else if (enumSet.contains(BlockPathTypes.UNPASSABLE_RAIL)) {
            return BlockPathTypes.UNPASSABLE_RAIL;
        } else {
            BlockPathTypes blockPathTypes2 = BlockPathTypes.BLOCKED;
            Iterator var9 = enumSet.iterator();

            while (var9.hasNext()) {
                BlockPathTypes blockPathTypes3 = (BlockPathTypes) var9.next();
                if (mob.getPathfindingMalus(blockPathTypes3) < 0.0F) {
                    return blockPathTypes3;
                }

                if (mob.getPathfindingMalus(blockPathTypes3) >= mob.getPathfindingMalus(blockPathTypes2)) {
                    blockPathTypes2 = blockPathTypes3;
                }
            }

            return blockPathTypes == BlockPathTypes.OPEN && mob.getPathfindingMalus(blockPathTypes2) == 0.0F && this.entityWidth <= 1 ? BlockPathTypes.OPEN : blockPathTypes2;
        }
    }

    public BlockPathTypes getBlockPathTypes(BlockGetter world, int x, int y, int z, EnumSet<BlockPathTypes> nearbyTypes, BlockPathTypes type, BlockPos pos) {
        for (int i = 0; i < this.entityWidth; ++i) {
            for (int j = 0; j < this.entityHeight; ++j) {
                for (int k = 0; k < this.entityDepth; ++k) {
                    int l = i + x;
                    int m = j + y;
                    int n = k + z;
                    BlockPathTypes blockPathTypes = this.getBlockPathType(world, l, m, n);
                    blockPathTypes = this.evaluateBlockPathType(world, pos, blockPathTypes);
                    if (i == 0 && j == 0 && k == 0) {
                        type = blockPathTypes;
                    }

                    nearbyTypes.add(blockPathTypes);
                }
            }
        }

        return type;
    }

    protected BlockPathTypes evaluateBlockPathType(BlockGetter world, BlockPos pos, BlockPathTypes type) {
        boolean bl = this.canPassDoors();
        if (type == BlockPathTypes.DOOR_WOOD_CLOSED && this.canOpenDoors() && bl) {
            type = BlockPathTypes.WALKABLE_DOOR;
        }

        if (type == BlockPathTypes.DOOR_OPEN && !bl) {
            type = BlockPathTypes.BLOCKED;
        }

        if (type == BlockPathTypes.RAIL && !(world.getBlockState(pos).getBlock() instanceof BaseRailBlock) && !(world.getBlockState(pos.below()).getBlock() instanceof BaseRailBlock)) {
            type = BlockPathTypes.UNPASSABLE_RAIL;
        }

        return type;
    }

    protected BlockPathTypes getBlockPathType(SereneHumanEntity entity, BlockPos pos) {
        return this.getCachedBlockType(entity, pos.getX(), pos.getY(), pos.getZ());
    }

    protected BlockPathTypes getCachedBlockType(SereneHumanEntity entity, int x, int y, int z) {
        return (BlockPathTypes) this.pathTypesByPosCache.computeIfAbsent(BlockPos.asLong(x, y, z), (l) -> {
            return this.getBlockPathType(this.level, x, y, z, entity);
        });
    }

    public BlockPathTypes getBlockPathType(BlockGetter world, int x, int y, int z) {
        return getBlockPathTypeStatic(world, new BlockPos.MutableBlockPos(x, y, z));
    }
}

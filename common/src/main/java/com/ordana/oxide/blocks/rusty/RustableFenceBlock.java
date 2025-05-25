package com.ordana.oxide.blocks.rusty;

import com.google.common.collect.ImmutableMap;
import com.ordana.oxide.reg.ModBlockProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.WallSide;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Iterator;
import java.util.Map;

public class RustableFenceBlock extends WallBlock implements Rustable {
    public static final BooleanProperty VARNISHED = ModBlockProperties.VARNISHED;
    private final RustLevel rustLevel;
    private final Map<BlockState, VoxelShape> shapeByIndex;
    private final Map<BlockState, VoxelShape> collisionShapeByIndex;

    public RustableFenceBlock(RustLevel rustLevel, Properties settings) {
        super(Rustable.setRandomTicking(settings, rustLevel));
        this.shapeByIndex = this.makeShapes(2.0F, 2.0F, 16.0F, 0.0F, 16.0F, 16.0F);
        this.collisionShapeByIndex = this.makeShapes(2.0F, 2.0F, 24.0F, 0.0F, 24.0F, 24.0F);
        this.rustLevel = rustLevel;

        this.registerDefaultState(this.stateDefinition.any().setValue(VARNISHED, false));
    }

    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (entity instanceof LivingEntity liv) entity.causeFallDamage(Math.min(fallDistance, (liv.getHealth() / 2) + 2f), 2.0F, level.damageSources().stalagmite());
    }

    @Override
    public RustLevel getAge() {
        return this.rustLevel;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel serverLevel, BlockPos pos, RandomSource random) {
        if (!state.getValue(VARNISHED)) this.tryWeather(state, serverLevel, pos, random);
    }

    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return this.shapeByIndex.get(state);
    }

    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return this.collisionShapeByIndex.get(state);
    }

    private Map<BlockState, VoxelShape> makeShapes(float width, float depth, float wallPostHeight, float wallMinY, float wallLowHeight, float wallTallHeight) {
        float f = 8.0F - width;
        float g = 8.0F + width;
        float h = 8.0F - depth;
        float i = 8.0F + depth;
        VoxelShape voxelShape = Block.box(f, 0.0, f, g, wallPostHeight, g);
        VoxelShape voxelShape2 = Block.box(h, wallMinY, 0.0, i, wallLowHeight, i);
        VoxelShape voxelShape3 = Block.box(h, wallMinY, h, i, wallLowHeight, 16.0);
        VoxelShape voxelShape4 = Block.box(0.0, wallMinY, h, i, wallLowHeight, i);
        VoxelShape voxelShape5 = Block.box(h, wallMinY, h, 16.0, wallLowHeight, i);
        VoxelShape voxelShape6 = Block.box(h, wallMinY, 0.0, i, wallTallHeight, i);
        VoxelShape voxelShape7 = Block.box(h, wallMinY, h, i, wallTallHeight, 16.0);
        VoxelShape voxelShape8 = Block.box(0.0, wallMinY, h, i, wallTallHeight, i);
        VoxelShape voxelShape9 = Block.box(h, wallMinY, h, 16.0, wallTallHeight, i);
        ImmutableMap.Builder<BlockState, VoxelShape> builder = ImmutableMap.builder();
        Iterator var21 = UP.getPossibleValues().iterator();

        while(var21.hasNext()) {
            Boolean boolean_ = (Boolean)var21.next();
            Iterator var23 = EAST_WALL.getPossibleValues().iterator();

            while(var23.hasNext()) {
                WallSide wallSide = (WallSide)var23.next();
                Iterator var25 = NORTH_WALL.getPossibleValues().iterator();

                while(var25.hasNext()) {
                    WallSide wallSide2 = (WallSide)var25.next();
                    Iterator var27 = WEST_WALL.getPossibleValues().iterator();

                    while(var27.hasNext()) {
                        WallSide wallSide3 = (WallSide)var27.next();
                        Iterator var29 = SOUTH_WALL.getPossibleValues().iterator();

                        while(var29.hasNext()) {
                            WallSide wallSide4 = (WallSide)var29.next();
                            VoxelShape voxelShape10 = Shapes.empty();
                            voxelShape10 = applyWallShape(voxelShape10, wallSide, voxelShape5, voxelShape9);
                            voxelShape10 = applyWallShape(voxelShape10, wallSide3, voxelShape4, voxelShape8);
                            voxelShape10 = applyWallShape(voxelShape10, wallSide2, voxelShape2, voxelShape6);
                            voxelShape10 = applyWallShape(voxelShape10, wallSide4, voxelShape3, voxelShape7);
                            if (boolean_) {
                                voxelShape10 = Shapes.or(voxelShape10, voxelShape);
                            }

                            BlockState blockState = this.defaultBlockState().setValue(UP, boolean_).setValue(EAST_WALL, wallSide).setValue(WEST_WALL, wallSide3).setValue(NORTH_WALL, wallSide2).setValue(SOUTH_WALL, wallSide4);
                            builder.put(blockState.setValue(WATERLOGGED, false).setValue(VARNISHED, false), voxelShape10);
                            builder.put(blockState.setValue(WATERLOGGED, true).setValue(VARNISHED, false), voxelShape10);
                            builder.put(blockState.setValue(WATERLOGGED, false).setValue(VARNISHED, true), voxelShape10);
                            builder.put(blockState.setValue(WATERLOGGED, true).setValue(VARNISHED, true), voxelShape10);
                        }
                    }
                }
            }
        }

        return builder.build();
    }

    private static VoxelShape applyWallShape(VoxelShape baseShape, WallSide height, VoxelShape lowShape, VoxelShape tallShape) {
        if (height == WallSide.TALL) {
            return Shapes.or(baseShape, tallShape);
        } else {
            return height == WallSide.LOW ? Shapes.or(baseShape, lowShape) : baseShape;
        }
    }

    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return this.use(stack, state, level, pos, player, hand, hitResult);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(UP, NORTH_WALL, EAST_WALL, WEST_WALL, SOUTH_WALL, WATERLOGGED, VARNISHED);
    }
}

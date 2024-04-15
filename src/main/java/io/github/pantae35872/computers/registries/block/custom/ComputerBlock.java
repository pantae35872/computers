package io.github.pantae35872.computers.registries.block.custom;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import io.github.pantae35872.computers.Main;
import io.github.pantae35872.computers.registries.block_entity.ModBlockEntity;
import io.github.pantae35872.computers.registries.block_entity.custom.ComputerBlockEntity;
import net.minecraft.client.gui.screens.inventory.FurnaceScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.CommandBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.function.Function;

@SuppressWarnings("deprecation")
public class ComputerBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public static final MapCodec<FurnaceBlock> CODEC = simpleCodec(FurnaceBlock::new);

    public ComputerBlock() {
        super(BlockBehaviour.Properties.of().noOcclusion());
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState pState,
                                        @NotNull BlockGetter pLevel, @NotNull BlockPos pPos,
                                        @NotNull CollisionContext pContext) {
        VoxelShape shape = Shapes.empty();
        Direction facing = pState.getValue(FACING);
        if (facing == Direction.NORTH || facing == Direction.SOUTH) {
            shape = Shapes.join(shape, Shapes.box(0, 0, 0.125, 1, 1, 0.875), BooleanOp.OR);
        } else {
            shape = Shapes.join(shape, Shapes.box(0.125, 0, 0, 0.875, 1, 1), BooleanOp.OR);
        }

        return shape;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof ComputerBlockEntity computerBlockEntity) {
                computerBlockEntity.drops();
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            if(entity instanceof ComputerBlockEntity) {
                pPlayer.openMenu((MenuProvider) entity);
            } else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }
        return InteractionResult.sidedSuccess(pLevel.isClientSide());

    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pPos,
                                      @NotNull BlockState pState) {
        return new ComputerBlockEntity(pPos, pState);
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level pLevel,
                                                                  @NotNull BlockState pState,
                                                                  @NotNull BlockEntityType<T>
                                                                             pBlockEntityType) {
        if (pLevel.isClientSide()) {
            return null;
        }
        return createTickerHelper(pBlockEntityType, ModBlockEntity.COMPUTER.get(),
                ((pLevel1, pPos, pState1, pBlockEntity) -> {
                   pBlockEntity.tick(pLevel1, pPos, pState1);
                }));
    }
}

package com.ordana.oxide.items;

import com.ordana.oxide.reg.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class CementPowderBucketItem extends Item {
    public CementPowderBucketItem(Properties properties) {
        super(properties);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);
        BlockHitResult blockHitResult = getPlayerPOVHitResult(level, player, net.minecraft.world.level.ClipContext.Fluid.SOURCE_ONLY);

        if (blockHitResult.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(itemStack);
        } else if (blockHitResult.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(itemStack);
        } else {
            BlockPos blockPos = blockHitResult.getBlockPos();
            Direction direction = blockHitResult.getDirection();
            BlockPos blockPos2 = blockPos.relative(direction);
            if (level.mayInteract(player, blockPos) && player.mayUseItemAt(blockPos2, direction, itemStack)) {
                BlockState blockState = level.getBlockState(blockPos);
                if (blockState.getFluidState().is(Fluids.WATER)) {
                    player.setItemSlot(player.getEquipmentSlotForItem(itemStack), ModItems.CEMENT_BUCKET.get().getDefaultInstance());
                    level.playSound(player, BlockPos.containing(player.position()), SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1F, 1.5F + (level.random.nextFloat() * 0.5F));
                }
            }
        }
        return InteractionResultHolder.pass(itemStack);
    }
}

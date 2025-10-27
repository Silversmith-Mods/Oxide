package com.ordana.oxide.blocks.Dispenser;


import com.ordana.oxide.items.CementBucketItem;
import net.mehvahdjukaar.moonlight.api.util.DispenserHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.block.DispenserBlock;

public class CementBucketDispenserBehavior extends DispenserHelper.PlaceBlockBehavior {

    public CementBucketDispenserBehavior(Item item) {
        super(item);
    }

    protected InteractionResultHolder<ItemStack> customBehavior(BlockSource source, ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof CementBucketItem bi) {
            Direction direction = source.state().getValue(DispenserBlock.FACING);
            BlockPos blockpos = source.pos().relative(direction);
            if (bi.getAmount(stack) == 1) source.blockEntity().insertItem(new ItemStack(Items.BUCKET));
            InteractionResult result = bi.place(new DirectionalPlaceContext(source.level(), blockpos, direction, stack, direction));
            InteractionResultHolder<ItemStack> res = new InteractionResultHolder(result, stack);

            return res;
        }

        return InteractionResultHolder.pass(stack);
    }


}
package com.TMM.TooMuchMaterials;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

public class BlockCraftingBlock extends Block {

        public BlockCraftingBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useItemOn(
            net.minecraft.world.item.ItemStack stack,
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            net.minecraft.world.InteractionHand hand,
            BlockHitResult hitResult
    ) {
        if (!level.isClientSide()) {
            if (stack.isEmpty()) {
            
                player.sendSystemMessage(
                    Component.literal("RubyBlockNOItem")
                );

                Pig pig = EntityTypes.PIG.create(
                    level,
                    EntitySpawnReason.TRIGGERED
                );

                if (pig != null) {
                    pig.setPos(
                        pos.getX()+ 0.5,
                        pos.getY()+ 1.0,
                        pos.getZ()+ 0.5
                    );
                    level.addFreshEntity(pig);
                }

                return InteractionResult.SUCCESS;
            }

            else {
                player.sendSystemMessage(
                    Component.literal("RubyBlockItem")
                );
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.SUCCESS;
    }
    
}

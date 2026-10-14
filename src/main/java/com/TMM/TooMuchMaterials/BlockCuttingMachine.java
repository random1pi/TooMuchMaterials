package com.TMM.TooMuchMaterials;

import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.Screen;

public class BlockCuttingMachine extends Block implements EntityBlock{
    public BlockCuttingMachine(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {

        return new EntityCuttingMachine(
            pos,
            state
        );
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
        Level level,
        BlockState state,
        BlockEntityType<T> type
    ) {
        if (type != ModBlockEntities.CUTTING_MACHINE_ENTITY.get()) {return null;}
        return (lvl, pos, st, be) ->
            EntityCuttingMachine.tick(
                lvl,
                pos,
                st,
                (EntityCuttingMachine) be
            );

    }

    public void sendmessage(Level level, String text) {
                if (!(level instanceof ServerLevel serverLevel)) {return;}
        
        for (ServerPlayer player : serverLevel.players()) {
            player.sendSystemMessage(
                Component.literal(text)
            );
        }
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
       if (level.isClientSide()) {
        return InteractionResult.SUCCESS;
        }
        sendmessage(level, "right clicked");

        net.minecraft.client.Minecraft.getInstance()
            .gui.setScreen(new UICuttingMachine());

        return InteractionResult.SUCCESS; 
    }
}
 
package com.TMM.TooMuchMaterials;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;

public class EntityCuttingMachine extends BlockEntity{

    private int progress = 0;
    private ItemStack inputItem = ItemStack.EMPTY;
    private ItemStack outputItem = ItemStack.EMPTY;


    public EntityCuttingMachine(
        BlockPos pos,
        BlockState state
    ) {
        super(ModBlockEntities.CUTTING_MACHINE_ENTITY.get(), pos, state);
    }
    //progress
    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        setChanged();
    }
    //input
    public ItemStack getInput() {
        return inputItem;
    }

    public void setInput(ItemStack inputItem) {
        this.inputItem = inputItem;
        setChanged();
    }

    //input
    public ItemStack getOutput() {
        return outputItem;
    }

    public void setOutput(ItemStack outputItem) {
        this.outputItem = outputItem;
        setChanged();
    }

    public void sendmessage(String text) {

        if (!(level instanceof ServerLevel serverLevel)) {return;}
        
        for (ServerPlayer player : serverLevel.players()) {
            player.sendSystemMessage(
                Component.literal(text)
            );
        }

    }

    //Tick-----------------------
    public static void tick(
        Level level,
        BlockPos pos,
        BlockState state,
        EntityCuttingMachine entity
    ) {

        if ((entity.progress >= 100) && (entity.inputItem.isEmpty() || entity.inputItem == ItemStack.EMPTY)) {
            entity.progress = 0;
            entity.sendmessage("hi");


        }

        if ((entity.inputItem.isEmpty())) {
            entity.progress += 1;


        }
    }


    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("Progress", progress);
        if (!inputItem.isEmpty()) {
            output.store("InputItem", ItemStack.CODEC, inputItem);
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        progress = input.getIntOr("Progress", progress);
        inputItem = input.read("InputItem", ItemStack.CODEC).orElse(ItemStack.EMPTY);
    }
    
}

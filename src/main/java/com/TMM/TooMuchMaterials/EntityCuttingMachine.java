package com.TMM.TooMuchMaterials;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Items;
import net.minecraft.world.Containers;
import net.minecraft.world.item.Item;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import net.minecraft.core.Direction;

public class EntityCuttingMachine extends BlockEntity{

    private int progress = 0;
    private int maxProgress = 100;

    public static final int INPUT_SLOTS = 1;
    public static final int OUTPUT_SLOT = 0;
    public static final int FIRST_INPUT_SLOT = 1;
    public static final int TOTAL_SLOTS = 1 + INPUT_SLOTS;

    private static final int DEFAULT_MAX_PROGRESS = 100;

    private final ItemStacksResourceHandler inventory =
        new ItemStacksResourceHandler(TOTAL_SLOTS) {
            @Override 
            protected void onContentsChanged(
                int index,
                ItemStack previousContent
            ) {
                EntityCuttingMachine.this.setChanged();
            }
        };

    @Override 
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {//drops content
        super.preRemoveSideEffects(pos, state);
        if (level != null && !level.isClientSide()) {
            Containers.dropItemStack(
                level,
                pos.getX(),
                pos.getY(),
                pos.getZ(),
                inventory.getResource(FIRST_INPUT_SLOT)
                    .toStack(inventory.getAmountAsInt(FIRST_INPUT_SLOT))
            );
            setInput(ItemStack.EMPTY);

        }
    }

    private final ResourceHandler<ItemResource> automationInventory =
        new ResourceHandler<>() {
            @Override
            public int size() {
                return inventory.size();
            }

            @Override
            public ItemResource getResource(int index) {
                return inventory.getResource(index);
            }

            @Override
            public long getAmountAsLong(int index) {
                return inventory.getAmountAsLong(index);
            }

            @Override
            public long getCapacityAsLong(int index, ItemResource resource) {
                return inventory.getCapacityAsLong(index, resource);
            }

            @Override
            public boolean isValid(int index, ItemResource resource) {
                return index == FIRST_INPUT_SLOT && inventory.isValid(index, resource);
            }

            @Override
            public int insert(
                int index,
                ItemResource resource,
                int amount,
                TransactionContext transaction
            ) {
                if (index != FIRST_INPUT_SLOT) {
                    return 0;
                }
                return inventory.insert(index, resource, amount, transaction);
            }

            @Override
            public int extract(
                int index,
                ItemResource resource,
                int amount,
                TransactionContext transaction
            ) {
                if (index != OUTPUT_SLOT) {
                    return 0;
                }
                return inventory.extract(index, resource, amount, transaction);
            }
        };

        public ResourceHandler<ItemResource> getAutomationInventory() {
            return automationInventory;
        }
        


    public EntityCuttingMachine(
        BlockPos pos,
        BlockState state
    ) {
        super(ModBlockEntities.CUTTING_MACHINE_ENTITY.get(), pos, state);
    }

    public ItemStacksResourceHandler getInventory() {
        return inventory;
    }

    public int getProgress() {
        return progress;
    }

    public int getMaxProgress() {
        return maxProgress;
    }
    
    public void setProgress(int progress) {
        this.progress = Math.max(0, Math.min(progress, maxProgress));
        setChanged();
    }

    public ItemStack getInput() {
        return inventory.getResource(FIRST_INPUT_SLOT)
            .toStack(inventory.getAmountAsInt(FIRST_INPUT_SLOT));
    }

    public void setInput(ItemStack stack) {
        setStack(FIRST_INPUT_SLOT, stack);
    }

    public ItemStack getOutput() {
        return inventory.getResource(OUTPUT_SLOT)
            .toStack(inventory.getAmountAsInt(OUTPUT_SLOT));
    }

    public void setOutput(ItemStack stack) {
        setStack(OUTPUT_SLOT, stack);
    }

    private void setStack(int slot, ItemStack stack) {
        if (stack.isEmpty()) {
            inventory.set(slot, ItemResource.EMPTY, 0);
        } else {
            inventory.set(slot, ItemResource.of(stack), stack.getCount());
        }
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
    
    public long getPowerFromNeighbors(Level level, BlockPos pos) {
        long totalEnergy = 0;
        for (Direction direction : Direction.values()) {//check for energy
                    
            BlockPos neighborPos = pos.relative(direction);
            BlockEntity neighbor = level.getBlockEntity(neighborPos);
                

            if (neighbor instanceof EntityCableBlock cable) {
                totalEnergy += cable.getEnergy();
            }
        }
        return totalEnergy;
    }

    public void takePowerFromNeighbors(Level level, BlockPos pos, long energyCost) {
        for (Direction direction : Direction.values()) {
                    
            BlockPos neighborPos = pos.relative(direction);
            BlockEntity neighbor = level.getBlockEntity(neighborPos);
                
            if (neighbor instanceof EntityCableBlock cable) {//take from cable
                long cableEnergy = cable.getEnergy();
                if (cableEnergy >= energyCost) {
                    cable.takeEnergy(energyCost);
                    energyCost = 0;
                } else {
                    cable.takeEnergy(cableEnergy);
                    energyCost -= cableEnergy;
                }
            }
        }
    }


    //Tick-----------------------
    public static void tick(
        Level level,
        BlockPos pos,
        BlockState state,
        EntityCuttingMachine entity
    ) {

        ItemStack input = entity.getInput();
        ItemStack output = entity.getOutput();

        //recipe 1
        if (level.isClientSide()) {return;}

        if (!input.isEmpty() && input.is(Items.COPPER_INGOT)) { //recipe1

            boolean canOutput = output.isEmpty() ||
                (output.is(ModBlocks.COPPER_WIRE)) &&
                output.getCount() < output.getMaxStackSize();

            if (!canOutput) {
                return;
            }

            //---------------------------------------
            long energyCost = 1;//crafting cost
            //---------------------------------------

            long totalEnergy = entity.getPowerFromNeighbors(level, pos);

            if ((totalEnergy >= energyCost)) {//if energy there -> progress + take energy
                entity.progress ++;
                entity.takePowerFromNeighbors(level, pos, energyCost);
            }

            if (entity.progress >= entity.getMaxProgress()) {
                input.shrink(1);//apply recipe
                entity.setInput(input);
                if (output.isEmpty()) {
                    entity.setOutput(new ItemStack(ModBlocks.COPPER_WIRE.get(), 1));
                } else {
                    output.grow(1);
                    entity.setOutput(output);
                }
                entity.setProgress(0);

                //entity.takePowerFromNeighbors(level, pos, energyCost);
                
            }


        } else {
            if (entity.progress != 0) {
                entity.setProgress(0);
            }
        }
    }



    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("Progress", progress);
        output.putInt("MaxProgress", maxProgress);
        // Speichert die Slots über den Handler.
        inventory.serialize(output);

    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        progress = input.getIntOr("Progress", 0);
        maxProgress = Math.max(1, input.getIntOr("MaxProgress", DEFAULT_MAX_PROGRESS));
        inventory.deserialize(input);
        progress = Math.max(0, Math.min(progress, maxProgress));
    }
    
}

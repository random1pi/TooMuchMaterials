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
import net.minecraft.world.item.Item;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.energy.SimpleEnergyHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.minecraft.core.Direction;

public class EntityGenerator extends BlockEntity{

    private static final int CAPACITY = 1000;
    private static final int TRANSFER_RATE = 250;
    private static final int TRANSFER_SPEED = 5; //MUST be greater/equal 2; -> ticks mod speed needs 2 possible outcomes

    public static final int TOTAL_SLOTS = 1;
    public static final int INPUT_SLOTS = 1;
    public static final int FIRST_INPUT_SLOT = 0;

    private static final int DEFAULT_MAX_PROGRESS = 100;
    private static final int MAX_FUEL_TIME = 10000;

    private int progress = 0;
    private int maxProgress = 100;
    private int fuelTime = 0;

    private long transfer_north = 0;
    private long transfer_east = 0;
    private long transfer_south = 0;
    private long transfer_west = 0;
    private long transfer_up = 0;
    private long transfer_down = 0;

    private final ItemStacksResourceHandler inventory =
        new ItemStacksResourceHandler(TOTAL_SLOTS) {
            @Override 
            protected void onContentsChanged(
                int index,
                ItemStack previousContent
            ) {
                EntityGenerator.this.setChanged();
            }
        };

    //hopper tech
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
                return 0;
            }
        };

        public ResourceHandler<ItemResource> getAutomationInventory() {
            return automationInventory;
        }
    

    private final EnergyHandler energy =
        new SimpleEnergyHandler(CAPACITY);


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

    public int getFuelTime() {
        return fuelTime;
    }
    
    public void setFuelTime(int fuelTime) {
        this.fuelTime = Math.max(0, Math.min(fuelTime, MAX_FUEL_TIME));
        setChanged();
    }

    public ItemStacksResourceHandler getInventory() {
        return inventory;
    }

    public ItemStack getInput() {
        return inventory.getResource(FIRST_INPUT_SLOT)
            .toStack(inventory.getAmountAsInt(FIRST_INPUT_SLOT));
    }

    public void setInput(ItemStack stack) {
        setStack(FIRST_INPUT_SLOT, stack);
    }

    private void setStack(int slot, ItemStack stack) {
        if (stack.isEmpty()) {
            inventory.set(slot, ItemResource.EMPTY, 0);
        } else {
            inventory.set(slot, ItemResource.of(stack), stack.getCount());
        }
        setChanged();
    }


    public long getEnergy() {
        return energy.getAmountAsLong();
    }

    public void setEnergy(long energy) {

        energy = Math.max(0, Math.min(energy, CAPACITY));
        
        long current = this.energy.getAmountAsLong();

        try (var transaction = Transaction.openRoot()) {
            if (energy > current) {
                this.energy.insert((int) (energy - current), transaction);
            } else if (energy < current) {
                this.energy.extract((int) (current - energy), transaction);
            }

            transaction.commit();
        }

        this.setChanged();
    }

    public void addEnergy(long energy) {
        try (var transaction = Transaction.openRoot()) {
            this.energy.insert((int) energy, transaction);
            transaction.commit();
        }
        this.setChanged();
    }

    public void takeEnergy(long energy) {
        try (var transaction = Transaction.openRoot()) {
            this.energy.extract((int) energy, transaction);
            transaction.commit();
        }
        this.setChanged();
    }


    public long getNeighboringEnergy(Direction direction) {
        if (level == null) {return 0;}
        BlockPos neighborPos = worldPosition.relative(direction);
        BlockEntity neighbor = level.getBlockEntity(neighborPos);
        if (neighbor instanceof EntityGenerator cable) {
            return cable.getEnergy();
        }
        return 0;
    }

    private void setTransferNorth(long transfer) {
        this.transfer_north = transfer;
        this.setChanged();
    }
    private void setTransferEast(long transfer) {
        this.transfer_east = transfer;
        this.setChanged();
    }
    private void setTransferSouth(long transfer) {
        this.transfer_south = transfer;
        this.setChanged();
    }
    private void setTransferWest(long transfer) {
        this.transfer_west = transfer;
        this.setChanged();
    }
    private void setTransferUp(long transfer) {
        this.transfer_up = transfer;
        this.setChanged();
    }
    private void setTransferDown(long transfer) {
        this.transfer_down = transfer;
        this.setChanged();
    }

    public EntityGenerator(
        BlockPos pos,
        BlockState state
    ) {
        super(ModBlockEntities.GENERATOR_ENTITY.get(), pos, state);
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
        EntityGenerator entity
    ) {
        ItemStack input = entity.getInput();
        int fuelTime = entity.getFuelTime();
        int progress = entity.getProgress();

        if (level.isClientSide()) {return;}

        if (!input.isEmpty() && input.is(Items.COAL) && fuelTime == 0) { //burn coal -> 200 ticks burn time
            input.shrink(1);
            entity.setInput(input);
            entity.setFuelTime(200);
        }
        if (fuelTime > 0) {//decrease burn time
            fuelTime --;
            entity.setFuelTime(fuelTime);
        }
        if (fuelTime > 0) {//increase progress
            progress ++;
            entity.setProgress(progress);
        }
        if (progress >= DEFAULT_MAX_PROGRESS) {//generate power every xx ticks
            progress = 0;
            //push energy
            entity.addEnergy(512);
            entity.setProgress(progress);
        }

        //distribute power to cables
        long ticks = level.getGameTime();
        long ownEnergy = entity.getEnergy();
        int cableCount = 1;
        long totalEnergy = entity.getEnergy();

        if (ticks % TRANSFER_SPEED == 0) {
            //calculate
            for (Direction direction : Direction.values()) {
                
                BlockPos neighborPos = pos.relative(direction);
                BlockEntity neighbor = level.getBlockEntity(neighborPos);

                if (neighbor instanceof EntityCableBlock cable ) {
                    long neighborEnergy = cable.getEnergy();

                    if (neighborEnergy < ownEnergy) {
                        totalEnergy += neighborEnergy;
                        cableCount ++;
                    }
                }
            }
            long targetEnergy = totalEnergy / cableCount;//equalize energy between neighbor
            //long ownTarget = totalEnergy - (targetEnergy * (cableCount - 1));//get the remainder //not necessary

            for (Direction direction : Direction.values()) {
                    
                BlockPos neighborPos = pos.relative(direction);
                BlockEntity neighbor = level.getBlockEntity(neighborPos);

                if (neighbor instanceof EntityCableBlock cable ) {
                    long neighborEnergy = cable.getEnergy();

                    if (neighborEnergy < ownEnergy) {
                        if (direction == direction.NORTH) {
                            entity.setTransferNorth(targetEnergy - neighborEnergy);
                        }
                        if (direction == direction.EAST) {
                            entity.setTransferEast(targetEnergy - neighborEnergy);
                        }
                        if (direction == direction.SOUTH) {
                            entity.setTransferSouth(targetEnergy - neighborEnergy);
                        }
                        if (direction == direction.WEST) {
                            entity.setTransferWest(targetEnergy - neighborEnergy);
                        }
                        if (direction == direction.UP) {
                            entity.setTransferDown(targetEnergy - neighborEnergy);
                        }
                        if (direction == direction.DOWN) {
                            entity.setTransferUp(targetEnergy - neighborEnergy);
                        }
                    }
                }
            }
        }
        if (ticks % TRANSFER_SPEED == 1) {
            //transfer
            for (Direction direction : Direction.values()) {
                    
                BlockPos neighborPos = pos.relative(direction);
                BlockEntity neighbor = level.getBlockEntity(neighborPos);

                if (neighbor instanceof EntityCableBlock cable ) {
                    if (direction == Direction.NORTH) {
                        long transfer = Math.max(0l, Math.min(entity.transfer_north, TRANSFER_RATE));
                        cable.addEnergy(transfer);
                        entity.takeEnergy(transfer);
                    }
                    if (direction == Direction.EAST) {
                        long transfer = Math.max(0l, Math.min(entity.transfer_east, TRANSFER_RATE));
                        cable.addEnergy(transfer);
                        entity.takeEnergy(transfer);
                    }
                    if (direction == Direction.SOUTH) {
                        long transfer = Math.max(0l, Math.min(entity.transfer_south, TRANSFER_RATE));
                         cable.addEnergy(transfer);
                        entity.takeEnergy(transfer);
                    }
                    if (direction == Direction.WEST) {
                        long transfer = Math.max(0l, Math.min(entity.transfer_west, TRANSFER_RATE));
                        cable.addEnergy(transfer);
                        entity.takeEnergy(transfer);
                    }
                    if (direction == Direction.UP) {
                        long transfer = Math.max(0l, Math.min(entity.transfer_up, TRANSFER_RATE));
                        cable.addEnergy(transfer);
                        entity.takeEnergy(transfer);
                    }
                    if (direction == Direction.DOWN) {
                        long transfer = Math.max(0l, Math.min(entity.transfer_down, TRANSFER_RATE));
                        cable.addEnergy(transfer);
                        entity.takeEnergy(transfer);
                    }

                }
           
            }
            entity.setTransferNorth(0);
            entity.setTransferEast(0);
            entity.setTransferSouth(0);
            entity.setTransferWest(0);
            entity.setTransferUp(0);
            entity.setTransferDown(0);
        }

    }



    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putLong("Energy", energy.getAmountAsLong());
        // Speichert die Slots über den Handler.
        output.putInt("Progress", progress);
        output.putInt("MaxProgress", maxProgress);
        output.putInt("FuelTime", fuelTime);
        // Speichert die Slots über den Handler.
        inventory.serialize(output);

    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        long savedEnergy = 
            Math.max(0, Math.min(input.getLongOr("Energy",0L), CAPACITY ));
        try (var transaction = Transaction.openRoot()) {
            energy.insert((int) savedEnergy, transaction);
            transaction.commit();
        }
        progress = input.getIntOr("Progress", 0);
        maxProgress = Math.max(1, input.getIntOr("MaxProgress", DEFAULT_MAX_PROGRESS));
        fuelTime = Math.max(0, Math.min(input.getIntOr("FuelTime", 0), MAX_FUEL_TIME));
        inventory.deserialize(input);
        progress = Math.max(0, Math.min(progress, maxProgress));

    }
    
}

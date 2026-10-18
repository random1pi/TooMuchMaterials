package com.TMM.TooMuchMaterials;


import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;
import net.minecraft.world.inventory.DataSlot;


public class MenuGenerator extends AbstractContainerMenu {

    private final DataSlot progressSlot = DataSlot.standalone();
    private final DataSlot maxProgressSlot = DataSlot.standalone();

    private final EntityGenerator blockEntity;

    // Wird serverseitig mit der echten BlockEntity aufgerufen.
    public MenuGenerator(
            int containerId,
            Inventory playerInventory,
            EntityGenerator blockEntity
    ) {
        super(ModMenus.GENERATOR_MENU.get(), containerId);

        this.blockEntity = blockEntity;

        addDataSlot(progressSlot);
        addDataSlot(maxProgressSlot);

        // Fortschritt mit dem Client synchronisieren
        addDataSlot(DataSlot.shared(new int[] {
            blockEntity.getProgress(),
            blockEntity.getMaxProgress()
        }, 0));


        for (int i = 0; i < EntityGenerator.INPUT_SLOTS; i++) {
            addSlot(new ResourceHandlerSlot(
                    blockEntity.getInventory(),
                    blockEntity.getInventory()::set,
                    EntityGenerator.FIRST_INPUT_SLOT + i,
                    44 + (i % 3) * 18,
                    17 + (i / 3) * 18
            ));
        }

        // Spielerinventar: 3 Reihen mit je 9 Slots
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(
                        playerInventory,
                        col + row * 9 + 9,
                        8 + col * 18,
                        84 + row * 18
                ));
            }
        }

        // Hotbar
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(
                    playerInventory,
                    col,
                    8 + col * 18,
                    142
            ));
        }
    }

    // Wird auf dem Client aufgerufen.
    public MenuGenerator(
            int containerId,
            Inventory playerInventory,
            RegistryFriendlyByteBuf buffer
    ) {
        this(
                containerId,
                playerInventory,
                getBlockEntity(playerInventory, buffer.readBlockPos())
        );
    }

    private static EntityGenerator getBlockEntity(
            Inventory playerInventory,
            BlockPos pos
    ) {
        BlockEntity entity =
                playerInventory.player.level().getBlockEntity(pos);

        if (entity instanceof EntityGenerator generatorEntity) {
            return generatorEntity;
        }

        throw new IllegalStateException(
                "Keine GeneratorEntity bei " + pos
        );
    }

    @Override
    public void broadcastChanges() {
        if (blockEntity.getLevel() != null
            && !blockEntity.getLevel().isClientSide()) {
            progressSlot.set(blockEntity.getProgress());
            maxProgressSlot.set(blockEntity.getMaxProgress());
        }

    super.broadcastChanges();
}

    @Override
    public boolean stillValid(Player player) {
        return blockEntity.getLevel() != null
                && blockEntity.getLevel().getBlockEntity(
                        blockEntity.getBlockPos()
                ) == blockEntity
                && player.distanceToSqr(
                        Vec3.atCenterOf(blockEntity.getBlockPos())
                ) <= 64.0;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        if (index < 0 || index >= slots.size()) {
            return ItemStack.EMPTY;
        }

        Slot slot = slots.get(index);

        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();

        // Maschinen-Slots: 0 bis 5
        if (index < 6) {
            if (!moveItemStackTo(stack, 6, slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else {
            // Spielerinventar -> ausschließlich Input-Slots
            if (!moveItemStackTo(stack, 1, 6, false)) {
                return ItemStack.EMPTY;
            }
        }

        if (stack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        if (stack.getCount() == original.getCount()) {
            return ItemStack.EMPTY;
        }

        slot.onTake(player, stack);
        return original;
    }


    public int getProgress() {
        return progressSlot.get();
    }

    public int getMaxProgress() {
        return maxProgressSlot.get();
    }

    public EntityGenerator getBlockEntity() {
        return blockEntity;
    }
}
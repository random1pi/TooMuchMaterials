package com.TMM.TooMuchMaterials;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TestBlockEntity;
import net.neoforged.neoforge.registries.DeferredRegister;
import java.util.function.Supplier;

class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>>BLOCK_ENTITIES =
        DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, TooMuchMaterials.MODID);

public static final Supplier<BlockEntityType<EntityCuttingMachine>>
        CUTTING_MACHINE_ENTITY = BLOCK_ENTITIES.register(
            "cutting_machine_entity",
            () -> new BlockEntityType<>(
                EntityCuttingMachine::new,
                false,
                ModBlocks.CUTTING_MACHINE.get()
            )
        );

}
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

public static final Supplier<BlockEntityType<EntityCableBlock>>
        CABLE_BLOCK_ENTITY = BLOCK_ENTITIES.register(
            "cable_block_entity",
            () -> new BlockEntityType<>(
                EntityCableBlock::new,
                false,
                ModBlocks.CABLE_BLOCK.get()
            )
        );

public static final Supplier<BlockEntityType<EntityGenerator>>
        GENERATOR_ENTITY = BLOCK_ENTITIES.register(
            "generator_entity",
            () -> new BlockEntityType<>(
                EntityGenerator::new,
                false,
                ModBlocks.GENERATOR.get()
            )
        );

}
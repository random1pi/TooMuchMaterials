package com.TMM.TooMuchMaterials;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

public class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks("tmm");

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems("tmm");

        //--------------------------------Blocks------------------------------------
    public static final DeferredBlock<Block> CRAFTING_BLOCK =
            BLOCKS.registerBlock(
                    "crafting_block",
                    BlockCraftingBlock::new,
                    properties -> properties.strength(3.0f, 6.0f)
            );

    public static final DeferredItem<?> CRAFTING_BLOCK_ITEM =
                ITEMS.registerSimpleBlockItem(CRAFTING_BLOCK);

   public static final DeferredBlock<BlockCuttingMachine> CUTTING_MACHINE =
                BLOCKS.register("cutting_machine", registryName -> 
                        new BlockCuttingMachine(
                                BlockBehaviour.Properties.of()
                                        .setId(
                                                ResourceKey.create(Registries.BLOCK, registryName)
                                        )
                                        .strength(3.0f, 6.0f)
                        )

                );

    public static final DeferredItem<?> CUTTING_MACHINE_ITEM =
                ITEMS.registerSimpleBlockItem(CUTTING_MACHINE);

   public static final DeferredBlock<BlockCableBlock> CABLE_BLOCK =
                BLOCKS.register("cable_block", registryName -> 
                        new BlockCableBlock(
                                BlockBehaviour.Properties.of()
                                        .setId(
                                                ResourceKey.create(Registries.BLOCK, registryName)
                                        )
                                        .strength(3.0f, 6.0f)
                        )

                );

    public static final DeferredItem<?> CABLE_BLOCK_ITEM =
                ITEMS.registerSimpleBlockItem(CABLE_BLOCK);

   public static final DeferredBlock<BlockGenerator> GENERATOR =
                BLOCKS.register("generator", registryName -> 
                        new BlockGenerator(
                                BlockBehaviour.Properties.of()
                                        .setId(
                                                ResourceKey.create(Registries.BLOCK, registryName)
                                        )
                                        .strength(3.0f, 6.0f)
                        )

                );

    public static final DeferredItem<?> GENERATOR_ITEM =
                ITEMS.registerSimpleBlockItem(GENERATOR);
        //----------------------------------ITEMS-----------------------------------
    public static final DeferredItem<Item> COPPER_WIRE =
                ITEMS.registerSimpleItem("copper_wire");

    public static final DeferredItem<Item> WIRE_CUTTER =
                ITEMS.register( "wire_cutter", () ->
                        new ItemWireCutter(
                                new Item.Properties()
                                        .durability(256)
                                        .setId(ResourceKey.create(
                                                Registries.ITEM,
                                                Identifier.fromNamespaceAndPath(TooMuchMaterials.MODID, "wire_cutter")
                                              )
                                        )
                        )
                );

    public static final DeferredItem<Item> ELECTRO_MAGNET_BAD =
                ITEMS.registerSimpleItem("electro_magnet_bad");

    public static final DeferredItem<Item> RELAIS =
                ITEMS.registerSimpleItem("relais");

    public static final DeferredItem<Item> CIRCUIT_BOARD_LV1 =
                ITEMS.registerSimpleItem("circuit_board_lv1");



}

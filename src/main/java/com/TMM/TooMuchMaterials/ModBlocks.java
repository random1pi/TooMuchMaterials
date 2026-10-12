package com.TMM.TooMuchMaterials;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.world.item.Item;

public class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks("examplemod");

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems("examplemod");


    public static final DeferredBlock<Block> CRAFTING_BLOCK =
            BLOCKS.registerBlock(
                    "crafting_block",
                    BlockCraftingBlock::new,
                    properties -> properties.strength(3.0f, 6.0f)
            );

    public static final DeferredItem<?> CRAFTING_BLOCK_ITEM =
                ITEMS.registerSimpleBlockItem(CRAFTING_BLOCK);

    public static final DeferredItem<Item> COPPER_WIRE =
                ITEMS.registerSimpleItem("copper_wire");




}

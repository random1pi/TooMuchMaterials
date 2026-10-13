package com.TMM.TooMuchMaterials;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.network.chat.Component;

public class ModTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TooMuchMaterials.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MATERIALS_TAB = CREATIVE_MODE_TABS.register("materials", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.materials")) //The language key for the title of your CreativeModeTab
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .icon(() -> ModBlocks.COPPER_WIRE.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(ModBlocks.COPPER_WIRE.get());// Add the example item to the tab. For your own tabs, this method is preferred over the event
                output.accept(ModBlocks.ELECTRO_MAGNET_BAD.get());
                output.accept(ModBlocks.RELAIS.get());
                output.accept(ModBlocks.CIRCUIT_BOARD_LV1.get());

            }).build());



    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> PROCESSING_TAB = CREATIVE_MODE_TABS.register("processing", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.processing")) //The language key for the title of your CreativeModeTab
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .icon(() -> ModBlocks.CRAFTING_BLOCK_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(ModBlocks.CRAFTING_BLOCK_ITEM.get());// Add the example item to the tab. For your own tabs, this method is preferred over the event

            }).build());

        public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TOOL_TAB = CREATIVE_MODE_TABS.register("tool", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.tool")) //The language key for the title of your CreativeModeTab
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .icon(() -> ModBlocks.WIRE_CUTTER.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(ModBlocks.WIRE_CUTTER.get());// Add the example item to the tab. For your own tabs, this method is preferred over the event

            }).build());
    
}

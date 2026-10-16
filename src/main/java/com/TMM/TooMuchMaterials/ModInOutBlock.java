package com.TMM.TooMuchMaterials;

import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class ModInOutBlock {

    public static void registerCapabilities(
            RegisterCapabilitiesEvent event
    ) {
        event.registerBlockEntity(
                Capabilities.Item.BLOCK,
                ModBlockEntities.CUTTING_MACHINE_ENTITY.get(),
                (machine, side) -> machine.getAutomationInventory()
        );
    }
}
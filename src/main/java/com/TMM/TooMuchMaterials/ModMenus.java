package com.TMM.TooMuchMaterials;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModMenus {

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(
                    Registries.MENU,
                    TooMuchMaterials.MODID
            );

    public static final Supplier<MenuType<MenuCuttingMachine>> CUTTING_MENU =
            MENUS.register(
                    "cutting_menu",
                    () -> IMenuTypeExtension.create(MenuCuttingMachine::new)
            );

    public static final Supplier<MenuType<MenuGenerator>> GENERATOR_MENU =
            MENUS.register(
                    "generator_menu",
                    () -> IMenuTypeExtension.create(MenuGenerator::new)
            );

}

package com.TMM.TooMuchMaterials;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModRecipeSerializers {

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(
                    Registries.RECIPE_SERIALIZER,
                    TooMuchMaterials.MODID
            );


    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<RecipeWireCutterShaped>> CUTTING_MACHINE_SHAPED =
            SERIALIZERS.register(
                    "cutting_machine_shaped",
                    () -> RecipeWireCutterShaped.SERIALIZER
            );
}
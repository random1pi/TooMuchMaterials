package com.TMM.TooMuchMaterials.mixin;

import org.spongepowered.asm.mixin.Mixin;
import com.TMM.TooMuchMaterials.ModBlocks;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RepairItemRecipe;

@Mixin(RepairItemRecipe.class)
public class RepairItemRecipeMixin {
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        NonNullList<ItemStack> remaining = CraftingRecipe.defaultCraftingReminder(input);
        int wireCutterCount = 0;
        for (int i = 0; i < input.size(); i++) {
            if (input.getItem(i).is(ModBlocks.WIRE_CUTTER.get())) {
                wireCutterCount ++;
            }
        }
        if (wireCutterCount == 2) {
            for (int i = 0; i < input.size(); i++) {
                if (input.getItem(i).is(ModBlocks.WIRE_CUTTER.get())) {
                    remaining.set(i, ItemStack.EMPTY);
                }
            }
        }
        return remaining;
    }
    
}

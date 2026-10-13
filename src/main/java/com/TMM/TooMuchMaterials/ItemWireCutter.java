package com.TMM.TooMuchMaterials;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.core.component.DataComponents;

public class ItemWireCutter extends Item{
    public ItemWireCutter(Properties properties) {
        super(properties);
    }
    @Override 
    public ItemStackTemplate getCraftingRemainder(ItemInstance instance) {
        ItemStack tool = new ItemStack(this);
        int damage = instance.getOrDefault(DataComponents.DAMAGE, 0) + 1;

        tool.setDamageValue(damage);

        if (damage >= tool.getMaxDamage()) {
            return null;
        }
        return ItemStackTemplate.fromNonEmptyStack(tool);
    }
 
}

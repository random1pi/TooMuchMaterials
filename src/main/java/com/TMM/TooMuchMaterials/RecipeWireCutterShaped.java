package com.TMM.TooMuchMaterials;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.NormalCraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

public class RecipeWireCutterShaped extends NormalCraftingRecipe{

    public static final MapCodec<RecipeWireCutterShaped> MAP_CODEC =
    RecordCodecBuilder.mapCodec(i -> i.group(
        Recipe.CommonInfo.MAP_CODEC.forGetter(o -> o.commonInfo),
        CraftingRecipe.CraftingBookInfo.MAP_CODEC.forGetter(o -> o.bookInfo),
        ShapedRecipePattern.MAP_CODEC.forGetter(o -> o.pattern),
        ItemStackTemplate.CODEC.fieldOf("result").forGetter(o -> o.result)
    ).apply(i, RecipeWireCutterShaped::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, RecipeWireCutterShaped> STREAM_CODEC =
        StreamCodec.composite(
            Recipe.CommonInfo.STREAM_CODEC,
            o -> o.commonInfo,
            CraftingRecipe.CraftingBookInfo.STREAM_CODEC,
            o -> o.bookInfo,
            ShapedRecipePattern.STREAM_CODEC,
            o -> o.pattern,
            ItemStackTemplate.STREAM_CODEC,
            o -> o.result,
            RecipeWireCutterShaped::new
        );

    public static final RecipeSerializer<RecipeWireCutterShaped> SERIALIZER =
        new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    public final ShapedRecipePattern pattern;
    private final ItemStackTemplate result;
    private final ShapedRecipe delegate;

    public RecipeWireCutterShaped(
        Recipe.CommonInfo commonInfo,
        CraftingRecipe.CraftingBookInfo bookInfo,
        ShapedRecipePattern pattern,
        ItemStackTemplate result
    ) {
        super(commonInfo, bookInfo);
        this.pattern = pattern;
        this.result = result;
        this.delegate = new ShapedRecipe(commonInfo, bookInfo, pattern, result);
    }

    @Override 
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        NonNullList<ItemStack> remaining = super.getRemainingItems(input);
        for (int i = 0; i < input.size(); i++) {
            if (input.getItem(i).is(ModBlocks.WIRE_CUTTER.get())) {
                remaining.set(i, ItemStack.EMPTY);
            }
        }
        return remaining;
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        return delegate.matches(input, level);
    }

    @Override 
    public ItemStack assemble(CraftingInput input) {
        return delegate.assemble(input);
    }

    @Override
    protected PlacementInfo createPlacementInfo() {
        return delegate.placementInfo();
    }

    @Override
    public RecipeSerializer<RecipeWireCutterShaped> getSerializer() {
        return SERIALIZER;
    }

    
}

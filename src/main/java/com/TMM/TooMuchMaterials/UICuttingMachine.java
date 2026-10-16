package com.TMM.TooMuchMaterials;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class UICuttingMachine extends AbstractContainerScreen<MenuCuttingMachine> {

    // Vanilla-Furnace-Textur
    private static final Identifier TEXTURE =
        Identifier.fromNamespaceAndPath(
            "minecraft",
            "textures/gui/container/furnace.png"
        );

    private static final Identifier ARROW_TEXTURE =
        Identifier.fromNamespaceAndPath(
                "minecraft",
                "textures/gui/sprites/container/furnace/burn_progress.png"
        );

    public static final int GUI_WIDTH = 176;
    public static final int GUI_HEIGHT = 176;

    private static final int ARROW_X = 79;
    private static final int ARROW_Y = 34;

    public UICuttingMachine(
        MenuCuttingMachine menu,
        Inventory playerInventory,
        Component title

    ) {
        super(menu, playerInventory, title, GUI_WIDTH, GUI_HEIGHT);

        this.titleLabelX = 8;
        this.titleLabelY = 6;
        this.inventoryLabelX = 8;
        this.inventoryLabelY = 72;
    }

    @Override 
    public void extractBackground(
        GuiGraphicsExtractor graphics,
        int mouseX,
        int mouseY,
        float partialTick
    ) {
        graphics.blit(
            TEXTURE,
            leftPos,
            topPos,
            leftPos + 176,
            topPos + 166,
            0.0F,
            176.0F / 256.0F,
            0.0F,
            166.0F / 256.0F
        ); 

        // fortschritt:
        int progress = menu.getProgress();
        int maxProgress = Math.max(1, menu.getMaxProgress());

        int arrowWidth = 24;

        int arrowHeight = 16;

        int filledWidth = Math.min(arrowWidth, progress * arrowWidth / maxProgress);

        if (filledWidth > 0) {
            graphics.blit(
                ARROW_TEXTURE,
                leftPos + ARROW_X,
                topPos + ARROW_Y,
                leftPos + ARROW_X + filledWidth,
                topPos + ARROW_Y + arrowHeight,
                 0.0F,
                (float) filledWidth / arrowWidth,
                0.0F,
                1.0F
            );
        }

        //graphics.fill(45, 45, 176, 166, 0xC6C6C6FF);//tried to hide furnace flames
    }

    /*@Override 
    public void extractRenderState(
        GuiGraphicsExtractor graphics,
        int mouseX,
        int mouseY,
        float partialTick
    ) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);

        int x = (this.width - 176) / 2;
        int y = (this.width - 166) / 2;

        graphics.fill(x, y, x+176, y+166, 0xFF202020);//background
        graphics.fill(x, y, x+176, y+2, 0xFFFFFFFF);//border
        graphics.centeredText(
            this.font,
            "Cutting Machine",
            this.width / 2,
            y + 10,
            0xFFFFFFFF
        );//title
        graphics.text(
            this.font,
            "Hello",
            x + 10, y +35,
            0xFFFFFFFF
        );

    }*/
}
 
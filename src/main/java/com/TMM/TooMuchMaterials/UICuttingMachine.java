package com.TMM.TooMuchMaterials;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class UICuttingMachine extends Screen {

    public UICuttingMachine() {
        super(Component.literal("Cutting Machine"));
    }

    @Override 
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

    }
}
 
package com.TMM.TooMuchMaterials;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(
        modid = TooMuchMaterials.MODID,
        value = Dist.CLIENT
)
public class Client {

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(
                ModMenus.CUTTING_MENU.get(),
                UICuttingMachine::new
        );
    }
}

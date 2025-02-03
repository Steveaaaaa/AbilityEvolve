package yezi.skillablereforged.event;

import yezi.skillablereforged.Config;
import yezi.skillablereforged.client.Overlay;
import yezi.skillablereforged.client.screen.SkillScreen;
import yezi.skillablereforged.client.screen.buttons.KeyBinding;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(
        modid = "skillablereforged",
        value = {Dist.CLIENT}
)

public class ClientEvents {
    public ClientEvents() {
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (KeyBinding.SKILLS_KEY.isDown()) {
            minecraft.setScreen(new SkillScreen());
        }
    }

    @SubscribeEvent
    public static void registerOverlay(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("skill_page", new Overlay());
    }
}

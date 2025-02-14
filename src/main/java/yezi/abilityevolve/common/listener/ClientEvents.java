package yezi.abilityevolve.common.listener;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yezi.abilityevolve.client.Overlay;
import yezi.abilityevolve.client.screen.SkillScreen;
import yezi.abilityevolve.client.screen.buttons.KeyBinding;
import yezi.abilityevolve.common.abilities.EnergeticAbility;
import yezi.abilityevolve.common.capabilities.ModCapabilities;
import yezi.abilityevolve.common.skills.Skill;
import yezi.abilityevolve.common.utils.GetAbilityLevel;

//@Mod.EventBusSubscriber(modid = "abilityevolve", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
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
    @SubscribeEvent
    public static void onRenderHungerHUD(RenderGuiOverlayEvent.Pre event) {
        if (event.getOverlay().id().equals(VanillaGuiOverlay.FOOD_LEVEL.id())) {
            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;
            if (player == null) return;
            int level = GetAbilityLevel.getAbilityLevelFarming1(ModCapabilities.getSkillModel(player).getSkillLevel(Skill.FARMING), EnergeticAbility.requirement);
            FoodData foodData = player.getFoodData();
            float total = foodData.getFoodLevel() + foodData.getSaturationLevel();
            boolean useCustom = (total >= EnergeticAbility.calculateRequiredX(level));
            if (useCustom){
                event.setCanceled(true);

                renderCustomFood(event.getGuiGraphics(), mc, player, true);
            }
        }
    }

    private static void renderCustomFood(GuiGraphics guiGraphics, Minecraft mc, Player player, boolean useCustom) {
        ResourceLocation texture = new ResourceLocation("abilityevolve", "textures/gui/custom_food_icons.png");

        mc.getProfiler().push("food");
        RenderSystem.enableBlend();
        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();
        int left = width / 2 + 91;
        int top = height - 49;
        RandomSource random = RandomSource.create();

        FoodData stats = player.getFoodData();
        int level = stats.getFoodLevel();

        for (int i = 0; i < 10; ++i) {
            int idx = i * 2 + 1;
            int x = left - i * 8 - 9;
            int y = top;
            int icon = 16;
            byte background = 0;

            if (player.hasEffect(MobEffects.HUNGER)) {
                icon += 36;
                background = 13;
            }
            if (stats.getSaturationLevel() <= 0.0F && mc.gui.getGuiTicks() % (level * 3 + 1) == 0) {
                y = top + (random.nextInt(3) - 1);
            }

            guiGraphics.blit(
                    texture,
                    x, y,
                    16 + background * 9, 27,
                    9, 9
            );

            if (idx < level) {
                guiGraphics.blit(
                        texture,
                        x, y,
                        icon + 36, 27,
                        9, 9
                );
            } else if (idx == level) {
                guiGraphics.blit(
                        texture,
                        x, y,
                        icon + 45, 27, // 半满图标
                        9, 9
                );
            }
        }
        RenderSystem.disableBlend();
        mc.getProfiler().pop();
    }
}

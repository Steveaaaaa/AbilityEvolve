package yezi.abilityevolve.common.capabilities;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = "AbilityEvolve", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModCapabilities {
    public static final Capability<SkillModel> SKILL_MODEL_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<AbilityModel> ABILITY_MODEL_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(SkillModel.class);
        event.register(AbilityModel.class);
    }

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Player> event) {
        event.addCapability(
                new ResourceLocation("AbilityEvolve", "skill_model"),
                new SkillModelProvider()
        );
        event.addCapability(
                new ResourceLocation("AbilityEvolve", "ability_model"),
                new AbilityModelProvider()
        );
    }

    public static SkillModel getSkillModel(Player player) {
        return player.getCapability(SKILL_MODEL_CAPABILITY).orElseThrow(() -> new RuntimeException("SkillModel not found!"));
    }
    public static Optional<SkillModel> getOptionalSkillModel(Player player) {
        return player.getCapability(SKILL_MODEL_CAPABILITY).resolve();
    }

    public static AbilityModel getAbilityModel(Player player) {
        return player.getCapability(ABILITY_MODEL_CAPABILITY).orElseThrow(() -> new RuntimeException("AbilityModel not found!"));
    }
    public static Optional<AbilityModel> getOptionalAbilityModel(Player player) {
        return player.getCapability(ABILITY_MODEL_CAPABILITY).resolve();
    }
}
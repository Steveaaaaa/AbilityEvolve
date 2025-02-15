package yezi.abilityevolve.common.effects;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = "abilityevolve", bus = Mod.EventBusSubscriber.Bus.MOD)
public class DamageTypeRegistry {
    public static final RegistryObject<DamageType> TRUE_DAMAGE = RegistryObject.create(
            new ResourceLocation("abilityevolve", "true_damage"),
            Registries.DAMAGE_TYPE,
            "abilityevolve"
    );

    @SubscribeEvent
    public static void registerDamageTypes(RegisterEvent event) {
        event.register(Registries.DAMAGE_TYPE, helper -> {
            helper.register(TRUE_DAMAGE.getId(), new DamageType("abilityevolve.true_damage", 0.1F));
        });
    }
}

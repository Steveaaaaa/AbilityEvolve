package yezi.abilityevolve.common.abilities;

import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import yezi.abilityevolve.common.skills.Requirement;
import yezi.abilityevolve.common.skills.Skill;

import java.lang.reflect.Field;

public class EnchantedBladeAbility extends Ability {
    private static final String name = "enchanted_blade";
    private static final String description = "将近战伤害转化为魔法/真实伤害";
    public static final int requirement = 10;

    private static final int[] X_VALUES = {20, 22, 24, 26, 28, 31, 33, 35, 37, 40};
    private static final Field INVULNERABLE_FIELD;

    static {
        try {
            INVULNERABLE_FIELD = LivingEntity.class.getDeclaredField("invulnerableTime");
            INVULNERABLE_FIELD.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("无法访问无敌时间字段", e);
        }
    }

    public EnchantedBladeAbility() {
        super(
                name,
                description,
                new Requirement[]{
                        new Requirement(Skill.MAGIC.index, requirement)
                },
                "magic",
                0,
                4,
                true
        );
    }
    private static final ResourceKey<DamageType> TRUE_DAMAGE =
            ResourceKey.create(Registries.DAMAGE_TYPE,
                    new ResourceLocation("abilityevolve", "true_damage"));

    public static float calculateConvertRatio(int abilityLevel, Player player, LivingEntity target) {

        float attackDamage = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);
        float baseRatio = X_VALUES[abilityLevel - 1] / 100f;

        return target.getHealth() < attackDamage ? baseRatio * 1.2f : baseRatio;
    }

    @SuppressWarnings("unused")
    public static void applyConvertedDamage(Player player, LivingEntity target, float convertedDamage) {
        try {
            int originalInvul = (int) INVULNERABLE_FIELD.get(target);
            INVULNERABLE_FIELD.set(target, 0);

            DamageSource source = createDamageSource(player, target);
            target.hurt(source, convertedDamage);

            INVULNERABLE_FIELD.set(target, originalInvul);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("无法访问无敌时间字段", e);
        }
    }

    public static DamageSource createDamageSource(Player player, LivingEntity target) {
        RegistryAccess registryAccess = player.level().registryAccess();
        Registry<DamageType> damageTypes = registryAccess.registryOrThrow(Registries.DAMAGE_TYPE);

        return shouldApplyTrueDamage(player, target)
                ? new DamageSource(damageTypes.getHolderOrThrow(TRUE_DAMAGE), player)
                : player.damageSources().magic();
    }

    private static boolean shouldApplyTrueDamage(Player player, LivingEntity target) {
        float attackDamage = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);
        return target.getHealth() < attackDamage;
    }
}

package yezi.abilityevolve.common.abilities;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.phys.Vec3;
import yezi.abilityevolve.common.skills.Requirement;
import yezi.abilityevolve.common.skills.Skill;

public class EnergeticAbility extends Ability{
    private static final String name = "energetic";
    private static final String description = "When you are full, ur fast.";
    public static final int requirement = 20;
    public EnergeticAbility()
    {
        super(
                name,
                description,
                new Requirement[]{
                        new Requirement(
                                Skill.FARMING.index, requirement
                        ),
                        new Requirement(
                                Skill.AGILITY.index, 10
                        )
                },
                "farming",
                2,
                10,
                true
        );
    }
    private static final double BASE_X = 25.0;
    private static final double PER_LEVEL_REDUCTION = 0.5;
    private static final int MAX_LEVEL = 10;
    private static final MobEffect SPEED_EFFECT = MobEffects.MOVEMENT_SPEED;
    private static final int SPEED_LEVEL = 1;
    private static final MobEffect JUMP_EFFECT = MobEffects.JUMP;
    private static final int JUMP_LEVEL = 0;
    private static final int EFFECT_DURATION = 220;
    public static void applyEnergeticEffect(Player player, int abilityLevel) {
        if (abilityLevel < 1 || abilityLevel > MAX_LEVEL) return;

        double required = calculateRequiredX(abilityLevel);

        FoodData foodData = player.getFoodData();
        double total = foodData.getFoodLevel() + foodData.getSaturationLevel();

        if (total >= required) {
            applyEffects(player);
            showVisualFeedback(player, abilityLevel);
        }
    }
    public static double calculateRequiredX(int level) {
        return BASE_X - (level - 1) * PER_LEVEL_REDUCTION;
    }
    private static void applyEffects(Player player) {
        if (!player.level().isClientSide) {
            player.removeEffect(SPEED_EFFECT);
            player.removeEffect(JUMP_EFFECT);

            player.addEffect(new MobEffectInstance(
                    SPEED_EFFECT,
                    EFFECT_DURATION,
                    SPEED_LEVEL,
                    false, false, true
            ));

            player.addEffect(new MobEffectInstance(
                    JUMP_EFFECT,
                    EFFECT_DURATION,
                    JUMP_LEVEL,
                    false, false, true
            ));
        }
    }
    private static void showVisualFeedback(Player player, int level) {
        if (player.level() instanceof ServerLevel serverLevel) {
            Vec3 pos = player.position();

            int particleCount = 3 + level;
            serverLevel.sendParticles(
                    ParticleTypes.ELECTRIC_SPARK,
                    pos.x(),
                    pos.y() + 1.5,
                    pos.z(),
                    particleCount,
                    0.3, 0.2, 0.3,
                    0.05
            );

            if (player.tickCount % 20 == 0) {
                serverLevel.playSound(
                        null,
                        pos.x(), pos.y(), pos.z(),
                        SoundEvents.BEACON_AMBIENT,
                        SoundSource.PLAYERS,
                        0.5f + level * 0.05f,
                        0.8f + player.getRandom().nextFloat() * 0.4f
                );
            }
        }
    }
}

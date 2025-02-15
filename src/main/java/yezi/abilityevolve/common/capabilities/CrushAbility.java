package yezi.abilityevolve.common.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import yezi.abilityevolve.common.abilities.Ability;
import yezi.abilityevolve.common.skills.Requirement;
import yezi.abilityevolve.common.skills.Skill;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class CrushAbility extends Ability implements ICapabilitySerializable<CompoundTag> {
    private static final String name = "crush";
    private static final String description = "Strengthen your iron golem.";
    public static final int requirement = 14;
    public CrushAbility()
    {
        super(
                name,
                description,
                new Requirement[]{
                        new Requirement(Skill.MAGIC.index, requirement),
                        new Requirement(Skill.BUILDING.index, 10)
                },
                "magic",
                1,
                5,
                true
        );
    }

    private int currentCharge;
    private int chargeThreshold;
    private float damagePercent;
    private boolean activated = false;

    public void setParameters(int chargeThreshold, float damagePercent) {
        this.chargeThreshold = chargeThreshold;
        this.damagePercent = damagePercent;
        this.activated = true;
    }

    public void onHurt(LivingEntity entity) {
        if (!activated || chargeThreshold <= 0) return;

        if (++currentCharge >= chargeThreshold) {
            currentCharge = 0;
            triggerEffect(entity);
        }
    }

    private void triggerEffect(LivingEntity entity) {
        // 治疗
        entity.heal(entity.getMaxHealth() * 0.05F);

        // 伤害计算
        float attackDamage = 7.0F;
        if (entity.getAttribute(Attributes.ATTACK_DAMAGE) != null) {
            attackDamage = (float) entity.getAttributeValue(Attributes.ATTACK_DAMAGE);
        }
        final float finalDamage = attackDamage * (damagePercent / 100.0F);

        // 范围伤害
        Level level = entity.level();
        AABB aabb = entity.getBoundingBox().inflate(7.5);
        List<LivingEntity> targets = level.getEntitiesOfClass(
                LivingEntity.class,
                aabb,
                e -> e.isAlive() &&
                        !(e instanceof Villager) &&
                        !(e instanceof IronGolem)
        );

        for (LivingEntity target : targets) {
            target.hurt(entity.damageSources().mobAttack(entity), finalDamage);
        }
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("Charge", currentCharge);
        tag.putInt("Threshold", chargeThreshold);
        tag.putFloat("Percent", damagePercent);
        tag.putBoolean("Activated", activated);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        currentCharge = tag.getInt("Charge");
        chargeThreshold = tag.getInt("Threshold");
        damagePercent = tag.getFloat("Percent");
        activated = tag.getBoolean("Activated");
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return ModCapabilities.CRUSH_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> this));
    }
}

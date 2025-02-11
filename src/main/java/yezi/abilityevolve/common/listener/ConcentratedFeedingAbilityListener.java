package yezi.abilityevolve.common.listener;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Donkey;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.Mule;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yezi.abilityevolve.common.abilities.ConcentratedFeedingAbility;

public class ConcentratedFeedingAbilityListener {
    private final Player player;
    private final ConcentratedFeedingAbility concentratedFeedingAbility;
    public ConcentratedFeedingAbilityListener(Player player) {
        this.player = player;
        this.concentratedFeedingAbility = new ConcentratedFeedingAbility();
    }
    @SubscribeEvent
    public void onPlayerMount(EntityMountEvent event) {
        if (event.getEntityMounting() == this.player) {
            AbstractHorse mount = (AbstractHorse) event.getEntityBeingMounted();
            if (mount instanceof Horse || mount instanceof Donkey || mount instanceof Mule) {
                Double[] standardValues = concentratedFeedingAbility.getStandard();
                if (mount.getAttributeValue(Attributes.MOVEMENT_SPEED) <= standardValues[0]||mount.getAttributeValue(Attributes.JUMP_STRENGTH) <= standardValues[1])
                    concentratedFeedingAbility.applyMountEffects(mount);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerDismount(EntityMountEvent event) {
        if (event.getEntityMounting() == this.player) {
            LivingEntity entity = (LivingEntity) event.getEntityBeingMounted();
            if (entity instanceof Horse || entity instanceof Donkey || entity instanceof Mule) {
                concentratedFeedingAbility.removeMountEffects(entity);
            }
        }
    }
}


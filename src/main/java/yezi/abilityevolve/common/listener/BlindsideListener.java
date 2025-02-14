package yezi.abilityevolve.common.listener;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yezi.abilityevolve.common.abilities.BlindsideAbility;

public class BlindsideListener {
    private final ServerPlayer player;
    private final BlindsideAbility blindsideAbility;
    public BlindsideListener(ServerPlayer player) {
       // blindsideAbility.abilityLevel = GetAbilityLevel.getAbilityLevelDefense1(ModCapabilities.getSkillModel(player).getSkillLevel(Skill.DEFENSE), blindsideAbility.requiredSkill);
        this.player = player;
        this.blindsideAbility = new BlindsideAbility();
    }
    @SubscribeEvent
    public void onPlayerAttack(LivingDamageEvent event) {
        if (event.getSource().getEntity() != this.player) return;
        LivingEntity target = event.getEntity();
        float multiplier = blindsideAbility.getBonusDamage(target,this.player);
        event.setAmount(event.getAmount() * multiplier);
    }

}


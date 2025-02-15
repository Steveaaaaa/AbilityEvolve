package yezi.abilityevolve.common.listener;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TridentItem;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yezi.abilityevolve.common.abilities.EnchantedBladeAbility;
import yezi.abilityevolve.common.capabilities.ModCapabilities;
import yezi.abilityevolve.common.skills.Skill;
import yezi.abilityevolve.common.utils.GetAbilityLevel;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class EnchantedBladeListener {
    private static final Set<UUID> COOLDOWNS = new HashSet<>();

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getSource().getDirectEntity() instanceof Player player &&
                !event.getSource().is(DamageTypeTags.IS_PROJECTILE)) {

            if (COOLDOWNS.contains(player.getUUID())) return;

            ItemStack weapon = player.getMainHandItem();
            if (!isValidWeapon(weapon)) return;

            processDamageConversion(event, player, getSkillLevel(player));
        }
    }

    private static void processDamageConversion(LivingHurtEvent event, Player player, int abilityLevel) {
        try {
            COOLDOWNS.add(player.getUUID());

            LivingEntity target = event.getEntity();
            float originalDamage = event.getAmount();
            float convertRatio = EnchantedBladeAbility.calculateConvertRatio(abilityLevel, player, target);

            float convertedDamage = originalDamage * convertRatio;
            float remainingDamage = originalDamage - convertedDamage;

            event.setAmount(remainingDamage);

            if (convertedDamage > 0) {
                EnchantedBladeAbility.applyConvertedDamage(player, target, convertedDamage);
            }
        } finally {
            COOLDOWNS.remove(player.getUUID());
        }
    }

    private static boolean isValidWeapon(ItemStack stack) {
        return stack.getItem() instanceof SwordItem ||
                stack.getItem() instanceof AxeItem ||
                stack.getItem() instanceof TridentItem;
    }

    private static int getSkillLevel(Player player) {
        return GetAbilityLevel.getAbilityLevelMagic2(
                ModCapabilities.getSkillModel(player).getSkillLevel(Skill.MAGIC),
                EnchantedBladeAbility.requirement
        );
    }
}

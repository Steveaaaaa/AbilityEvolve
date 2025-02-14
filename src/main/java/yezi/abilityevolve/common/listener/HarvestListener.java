package yezi.abilityevolve.common.listener;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yezi.abilityevolve.common.abilities.HarvestAbility;
import yezi.abilityevolve.common.capabilities.ModCapabilities;
import yezi.abilityevolve.common.skills.Skill;
import yezi.abilityevolve.common.utils.GetAbilityLevel;

public class HarvestListener {
    private final Player player;
    public HarvestListener(Player player) {
        this.player = player;
    }
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (!(event.getSource().getEntity() instanceof Player player)) return;
        ItemStack weapon = player.getMainHandItem();
        if (!isHoe(weapon)) return;

        int abilityLevel = getSkillLevel(player);
        if (abilityLevel < 1) return;
        
        float extraDamage = HarvestAbility.calculateExtraDamage(abilityLevel, player);
        if (extraDamage <= 0) return;
        
        event.setAmount(event.getAmount() + extraDamage);
        HarvestAbility.applyHungerEffect(player);
    }
    
    private static boolean isHoe(ItemStack stack) {
        return stack.getItem() instanceof net.minecraft.world.item.HoeItem;
    }
    
    private static int getSkillLevel(Player player) {
         return GetAbilityLevel.getAbilityLevelFarming1(ModCapabilities.getSkillModel(player).getSkillLevel(Skill.FARMING), HarvestAbility.requirement);
    }
}

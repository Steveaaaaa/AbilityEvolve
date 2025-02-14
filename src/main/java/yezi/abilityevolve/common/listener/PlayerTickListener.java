package yezi.abilityevolve.common.listener;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yezi.abilityevolve.common.abilities.ConcealAbility;
import yezi.abilityevolve.common.abilities.EnergeticAbility;
import yezi.abilityevolve.common.abilities.LongTravelAbility;
import yezi.abilityevolve.common.capabilities.ModCapabilities;
import yezi.abilityevolve.common.skills.Skill;
import yezi.abilityevolve.common.utils.GetAbilityLevel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
@Mod.EventBusSubscriber
public class PlayerTickListener {
    public static final int CHECK_INTERVAL = 40;
    public static final Map<UUID, Boolean> energeticUnlockedMap = new HashMap<>();
    public static final Map<UUID, Boolean> longTravelUnlockedMap = new HashMap<>();
    public static final Map<UUID, Boolean> concealUnlockedMap = new HashMap<>();
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.side.isClient()) return;
        if (event.player.tickCount % CHECK_INTERVAL != 0) return;

        Player player = event.player;
        if (!(player instanceof ServerPlayer) ||
                player.isSpectator() ||
                !player.isAlive()
        ) return;
        if (energeticUnlockedMap.get(player.getUUID()))
            EnergeticAbility.applyEnergeticEffect(player, GetAbilityLevel.getAbilityLevelFarming1(ModCapabilities.getSkillModel(player).getSkillLevel(Skill.FARMING), EnergeticAbility.requirement));
        if (longTravelUnlockedMap.get(player.getUUID())){
            LongTravelAbility.applyHeightEffect(player, GetAbilityLevel.getAbilityLevelFarming1(ModCapabilities.getSkillModel(player).getSkillLevel(Skill.AGILITY), LongTravelAbility.requirement));
        }
        if (concealUnlockedMap.get(player.getUUID()))
            ConcealAbility.checkAndApplyStealth(player);
    }
}

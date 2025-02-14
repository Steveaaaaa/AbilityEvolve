package yezi.abilityevolve.common.listener;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yezi.abilityevolve.common.abilities.EnergeticAbility;
import yezi.abilityevolve.common.capabilities.ModCapabilities;
import yezi.abilityevolve.common.skills.Skill;
import yezi.abilityevolve.common.utils.GetAbilityLevel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
@Mod.EventBusSubscriber
public class EnergeticListener {
    public static final int CHECK_INTERVAL = 30;
    public static final Map<UUID, Boolean> abilityUnlockedMap = new HashMap<>();
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.side.isClient()) return;
        if (event.player.tickCount % CHECK_INTERVAL != 0) return;

        Player player = event.player;
        if (!(player instanceof ServerPlayer) ||
                player.isSpectator() ||
                !player.isAlive() ||
                !abilityUnlockedMap.get(player.getUUID())
        ) return;

        EnergeticAbility.applyEnergeticEffect(player, GetAbilityLevel.getAbilityLevelFarming1(ModCapabilities.getSkillModel(player).getSkillLevel(Skill.FARMING), EnergeticAbility.requirement));
    }
}

package yezi.abilityevolve.common.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.server.command.EnumArgument;
import yezi.abilityevolve.common.capabilities.ModCapabilities;
import yezi.abilityevolve.common.network.SyncConfigPacket;
import yezi.abilityevolve.common.particles.AbilityEvolveParticle;
import yezi.abilityevolve.common.skills.Skill;
import yezi.abilityevolve.config.SkillLockLoader;

import java.util.logging.Logger;

@EventBusSubscriber
public class SkillGetCommand {
    private static final Logger LOGGER = Logger.getLogger(SkillGetCommand.class.getName());

    public SkillGetCommand() {
    }

    static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("get").then(Commands.argument("player", EntityArgument.player()).then(Commands.argument("skill", EnumArgument.enumArgument(Skill.class)).executes(SkillGetCommand::execute)));
    }

    private static int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "player");
        Skill skill = context.getArgument("skill", Skill.class);
        context.getSource().sendSuccess(() -> Component.literal(skill.displayName).append(" " + ModCapabilities.getSkillModel(player).getSkillLevel(skill)), true);
        Minecraft.getInstance().level.addParticle(
                AbilityEvolveParticle.YELLOW_STAR.get(),
                player.getX(), player.getY() + 1, player.getZ(),
                0, 0, 0
        );
        return ModCapabilities.getSkillModel(player).getSkillLevel(skill);
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("skills")
                        .then(
                                Commands.literal("get")
                                        .then(
                                                Commands.argument("player", EntityArgument.player())
                                                        .then(
                                                                Commands.argument("skill", EnumArgument.enumArgument(Skill.class))
                                                                        .executes(SkillGetCommand::execute)
                                                        )
                                        )
                        )
                        .then(
                                Commands.literal("reload")
                                        .executes((context) -> {
                                            SkillLockLoader.load();
                                            context.getSource().sendSuccess(
                                                    () -> Component.literal("Skill configuration reloaded"), true
                                            );
                                            SyncConfigPacket.sendToAllClients();
                                            LOGGER.info("Executed /skills reload command and sent SyncConfigPacket to clients.");
                                            return 1;
                                        })
                        )
        );
    }
}

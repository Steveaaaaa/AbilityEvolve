package yezi.abilityevolve.common.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.command.EnumArgument;
import yezi.abilityevolve.config.ConfigManager;
import yezi.abilityevolve.common.capabilities.ModCapabilities;
import yezi.abilityevolve.common.network.SyncToClient;
import yezi.abilityevolve.common.skills.Skill;

public class SkillSetCommand {
    public SkillSetCommand() {
    }

    static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("set")
                .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.argument("skill", EnumArgument.enumArgument(Skill.class))
                                .then(Commands.argument("level", IntegerArgumentType.integer(1, ConfigManager.getMaxLevel()))
                                        .executes(SkillSetCommand::execute)
                                )
                        )
                );
    }


    private static int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "player");

        Skill skill = context.getArgument("skill", Skill.class);

        int level = IntegerArgumentType.getInteger(context, "level");

        ModCapabilities.getSkillModel(player).setSkillLevel(skill.index, level);

        SyncToClient.send(player);

        context.getSource().sendSuccess(() ->
                Component.literal(skill.displayName).append(" set to " + level), true);

        return 1;
    }

}

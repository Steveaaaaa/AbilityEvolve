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
import yezi.abilityevolve.Config;
import yezi.abilityevolve.common.capabilities.ModCapabilities;
import yezi.abilityevolve.common.network.SyncToClient;
import yezi.abilityevolve.common.skills.Skill;

public class SkillSetCommand {
    public SkillSetCommand() {
    }

    static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("set")  // 注册 "set" 命令
                .then(Commands.argument("player", EntityArgument.player())  // "player" 参数，用于指定玩家实体
                        .then(Commands.argument("skill", EnumArgument.enumArgument(Skill.class))  // "skill" 参数，用于指定技能
                                .then(Commands.argument("level", IntegerArgumentType.integer(1, Config.getMaxLevel()))  // "level" 参数，技能等级，范围是 1 到最大等级
                                        .executes(SkillSetCommand::execute)  // 执行 SkillSetCommand.execute 方法
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

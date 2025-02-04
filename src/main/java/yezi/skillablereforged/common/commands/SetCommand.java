package yezi.skillablereforged.common.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import yezi.skillablereforged.Config;
import yezi.skillablereforged.common.capabilities.SkillModel;
import yezi.skillablereforged.common.skills.Skill;
import yezi.skillablereforged.common.network.SyncToClient;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.command.EnumArgument;

public class SetCommand {
    public SetCommand() {
    }

    static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("set")  // 注册 "set" 命令
                .then(Commands.argument("player", EntityArgument.player())  // "player" 参数，用于指定玩家实体
                        .then(Commands.argument("skill", EnumArgument.enumArgument(Skill.class))  // "skill" 参数，用于指定技能
                                .then(Commands.argument("level", IntegerArgumentType.integer(1, Config.getMaxLevel()))  // "level" 参数，技能等级，范围是 1 到最大等级
                                        .executes(SetCommand::execute)  // 执行 SetCommand.execute 方法
                                )
                        )
                );
    }


    private static int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        // 获取命令参数 "player"，即指定的玩家
        ServerPlayer player = EntityArgument.getPlayer(context, "player");

        // 获取命令参数 "skill"，即指定的技能
        Skill skill = context.getArgument("skill", Skill.class);

        // 获取命令参数 "level"，即指定的技能等级
        int level = IntegerArgumentType.getInteger(context, "level");

        // 设置指定玩家的技能等级
        SkillModel.get(player).setSkillLevel(skill, level);

        // 向指定玩家同步技能信息
        SyncToClient.send(player);

        // 向执行命令的玩家发送反馈信息
        context.getSource().sendSuccess(() ->
                Component.literal(skill.displayName).append(" set to " + level), true);

        return 1;  // 返回成功的标志
    }

}

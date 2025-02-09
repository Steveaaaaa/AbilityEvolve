package yezi.abilityevolve.common.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.logging.Logger;
import yezi.abilityevolve.Config;
import yezi.abilityevolve.common.capabilities.SkillModel;
import yezi.abilityevolve.common.skills.Skill;
import yezi.abilityevolve.common.network.SyncConfigPacket;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.server.command.EnumArgument;

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
        Skill skill = (Skill) context.getArgument("skill", Skill.class);
        int level = SkillModel.get(player).getSkillLevel(skill);
        ((CommandSourceStack) context.getSource()).sendSuccess(() -> {
            return Component.literal(skill.displayName).append(" " + level);
        }, true);
        return level;
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("skills")  // 注册 "skills" 命令
                        .then(
                                Commands.literal("get")  // "get" 子命令
                                        .then(
                                                Commands.argument("player", EntityArgument.player())  // "player" 参数
                                                        .then(
                                                                Commands.argument("skill", EnumArgument.enumArgument(Skill.class))  // "skill" 参数
                                                                        .executes(SkillGetCommand::execute)  // 执行 SkillGetCommand.execute 方法
                                                        )
                                        )
                        )
                        .then(
                                Commands.literal("reload")  // "reload" 子命令
                                        .executes((context) -> {
                                            // 重新加载配置
                                            Config.load();

                                            // 向执行命令的玩家发送反馈消息
                                            ((CommandSourceStack) context.getSource()).sendSuccess(
                                                    () -> Component.literal("Skill configuration reloaded"), true
                                            );

                                            // 向所有客户端发送配置同步的消息
                                            SyncConfigPacket.sendToAllClients();

                                            // 日志输出
                                            LOGGER.info("Executed /skills reload command and sent SyncConfigPacket to clients.");

                                            return 1;  // 返回命令执行成功
                                        })
                        )
        );
    }
}

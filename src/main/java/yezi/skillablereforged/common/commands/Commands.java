package yezi.skillablereforged.common.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import yezi.skillablereforged.Config;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class Commands {
    public Commands() {
    }

    //@SubscribeEvent
    public static void onRegisterCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                LiteralArgumentBuilder.<CommandSourceStack>literal("skills")
                        .requires(source -> source.hasPermission(2)) // 权限检查
                        .then(SetCommand.register()) // 注册 SetCommand
                        .then(GetCommand.register()) // 注册 GetCommand
                        .then(
                                // 注册 "reload" 子命令
                                LiteralArgumentBuilder.<CommandSourceStack>literal("reload")
                                        .executes((context) -> {
                                            // 重新加载配置
                                            Config.load();

                                            // 向执行命令的玩家发送反馈消息
                                            ((CommandSourceStack) context.getSource()).sendSuccess(() -> Component.literal("Skill Configuration reloaded"), true);

                                            return 1; // 返回命令执行成功
                                        })
                        )
        );
    }
}
package yezi.abilityevolve.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import yezi.abilityevolve.Config;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public class Commands {
    public Commands() {
    }

    //@SubscribeEvent
    public static void onRegisterCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                LiteralArgumentBuilder.<CommandSourceStack>literal("skills")
                        .requires(source -> source.hasPermission(2)) // 权限检查
                        .then(SkillSetCommand.register()) // 注册 SkillSetCommand
                        .then(SkillGetCommand.register()) // 注册 SkillGetCommand
                        .then(
                                LiteralArgumentBuilder.<CommandSourceStack>literal("reload")
                                        .executes((context) -> {
                                            Config.load();
                                            ((CommandSourceStack) context.getSource()).sendSuccess(() -> Component.literal("Skill Configuration reloaded"), true);

                                            return 1;
                                        })
                        )
        );
    }
}
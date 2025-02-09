package yezi.abilityevolve.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import yezi.abilityevolve.config.SkillLockLoader;

public class Commands {
    public Commands() {
    }

    //@SubscribeEvent
    public static void onRegisterCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                LiteralArgumentBuilder.<CommandSourceStack>literal("skills")
                        .requires(source -> source.hasPermission(2))
                        .then(SkillSetCommand.register())
                        .then(SkillGetCommand.register())
                        .then(LiteralArgumentBuilder.<CommandSourceStack>literal("reload")
                                .executes((context) -> {
                                    SkillLockLoader.load();
                                    context.getSource().sendSuccess(() -> Component.literal("Skill Configuration reloaded"), true);
                                    return 1;
                                })
                        )
        );
    }
}
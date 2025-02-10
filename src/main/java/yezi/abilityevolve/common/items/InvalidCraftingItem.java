package yezi.abilityevolve.common.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class InvalidCraftingItem extends Item {

    public InvalidCraftingItem(Properties properties) {
        super(properties);
    }
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.literal("You can't craft this items, please get levels.").withStyle(ChatFormatting.DARK_GRAY));
    }
}


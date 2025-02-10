package yezi.abilityevolve.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yezi.abilityevolve.common.items.AbilityEvolveItems;
import yezi.abilityevolve.common.skills.SkillRequirementChecker;

import java.util.Objects;
import java.util.Optional;

@Mixin(CraftingMenu.class)
public class CraftingContainerMixin {
    @Inject(
            at = @At("HEAD"),
            method = "slotChangedCraftingGrid",
            cancellable = true
    )
    private static void onUpdateCraftingGrid(
            AbstractContainerMenu containerMenu,
            Level level,
            Player player,
            CraftingContainer craftingContainer,
            ResultContainer resultContainer,
            CallbackInfo ci) {
        if (!level.isClientSide) {
            ServerPlayer serverPlayer = (ServerPlayer) player;

            ItemStack craftResult = ItemStack.EMPTY;

            Optional<CraftingRecipe> optionalRecipe = Objects.requireNonNull(level.getServer())
                    .getRecipeManager()
                    .getRecipeFor(RecipeType.CRAFTING, craftingContainer, level);
            if (optionalRecipe.isPresent()) {
                CraftingRecipe craftingRecipe = optionalRecipe.get();
                craftResult = craftingRecipe.assemble(craftingContainer, level.registryAccess());
            }
            if (!craftResult.isEmpty() && !SkillRequirementChecker.canCraftItem(serverPlayer, craftResult)) {
                ci.cancel();

                ItemStack invalidItem = new ItemStack(AbilityEvolveItems.INVALID_CRAFTING_ITEM.get()); // Use the custom invalid item

                resultContainer.setItem(0, invalidItem); // Set the result slot to the invalid item

                containerMenu.slotsChanged(craftingContainer);

                serverPlayer.sendSystemMessage(Component.literal("合成的物品不符合技能要求！"));
            } else {
                resultContainer.setItem(0, craftResult);
            }
        }
    }
}


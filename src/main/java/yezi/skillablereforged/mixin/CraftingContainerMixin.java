package yezi.skillablereforged.mixin;

import yezi.skillablereforged.common.capabilities.SkillModel;
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

import java.util.Optional;

@Mixin(CraftingMenu.class)
public class CraftingContainerMixin {
    public CraftingContainerMixin() {
    }

    @Inject(
            at = {@At("HEAD")},
            method = {"slotChangedCraftingGrid"},
            cancellable = true
    )
    private static void onUpdateCraftingGrid(
            AbstractContainerMenu containerMenu,
            Level level,
            Player player,
            CraftingContainer craftingContainer,
            ResultContainer resultContainer,
            CallbackInfo ci) {
        System.out.println("1");
        if (!level.isClientSide) {
            ServerPlayer serverPlayer = (ServerPlayer) player;

            ItemStack craftResult = ItemStack.EMPTY;

            Optional<CraftingRecipe> optionalRecipe = level.getServer()
                    .getRecipeManager()
                    .getRecipeFor(RecipeType.CRAFTING, craftingContainer, level);
            if (optionalRecipe.isPresent()) {
                CraftingRecipe craftingRecipe = optionalRecipe.get();

                craftResult = craftingRecipe.assemble(craftingContainer, level.registryAccess());
            }
            System.out.println("2");

            if (!craftResult.isEmpty() && !SkillModel.get(serverPlayer).canCraftItem(serverPlayer, craftResult)) {
                // Skill is not enough, cancel the crafting operation
                ci.cancel();

                // Clear the crafting result
                resultContainer.setItem(0, ItemStack.EMPTY);

                // Return the items to the player's inventory (clear the crafting grid)
                for (int i = 0; i < craftingContainer.getContainerSize(); i++) {
                    ItemStack itemInSlot = craftingContainer.getItem(i);
                    if (!itemInSlot.isEmpty()) {
                        // Try to add the item to the player's inventory
                        player.getInventory().add(itemInSlot);
                        craftingContainer.setItem(i, ItemStack.EMPTY);  // Clear the slot
                    }
                }

                // Notify the container of changes
                containerMenu.slotsChanged(craftingContainer);

                System.out.println("3 - Items returned to player's inventory and crafting grid cleared.");
            }
        }
    }
}

package yezi.abilityevolve.common.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = "abilityevolve", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class AbilityEvolveItems {
    public static final String MODID = "abilityevolve";
    public static final DeferredRegister<Item> ABILITYEVOLVE_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final RegistryObject<Item> INVALID_CRAFTING_ITEM = ABILITYEVOLVE_ITEMS.register("invalid_crafting_item",
            () -> new InvalidCraftingItem(new Item.Properties()
                    .stacksTo(1)
                    .rarity(Rarity.EPIC)
                    .fireResistant()
            ));

    public static void register(IEventBus eventBus) {
        ABILITYEVOLVE_ITEMS.register(eventBus);
    }
}

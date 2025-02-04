package yezi.skillablereforged.common.listener;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.horse.Donkey;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.Mule;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yezi.skillablereforged.common.abilities.IronCavalryAbility;

public class IronCavalryListener {
    private final IronCavalryAbility ironCavalryAbility = new IronCavalryAbility();
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onPlayerMountIronCavalry(EntityMountEvent event) {
        if (event.getEntityMounting() instanceof ServerPlayer) {
            if (event.getEntityBeingMounted() instanceof Pig|| event.getEntityBeingMounted() instanceof Horse|| event.getEntityBeingMounted() instanceof Donkey || event.getEntityBeingMounted() instanceof Mule){
                ironCavalryAbility.applyAttackBonus((ServerPlayer) event.getEntityMounting());
            }
            if (event.getEntityBeingMounted() instanceof Pig)
                ironCavalryAbility.applyDefenseBonus((ServerPlayer) event.getEntityMounting(), (Pig) event.getEntityBeingMounted());
        }
    }
}

package yezi.skillablereforged.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import yezi.skillablereforged.Config;
import yezi.skillablereforged.common.capabilities.AbilityModel;
import yezi.skillablereforged.common.commands.abilities.Ability;
import yezi.skillablereforged.common.commands.abilities.AidSupportAbility;

import java.util.function.Supplier;

public class RequestGetAbility {
    private final String name;
    private final int index;

    public RequestGetAbility(Ability ability) {
        this.name = ability.name;
        this.index = ability.index;
    }
    public RequestGetAbility(FriendlyByteBuf buffer) {
        this.name = buffer.readUtf();
        this.index = buffer.readInt();
    }
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.name);
        buffer.writeInt(this.index);
    }
    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            assert player != null;

            AbilityModel abilityModel = AbilityModel.get(player);
            Ability ability = getAbilityByName(this.name);  // 根据 id 获取对应的 Ability 实例
          //  int level = abilityModel.getAbilityLevel(ability);
         //   int cost = calculateCost(level);  // 计算升级所需的费用
            if (ability.skillPointCost <= abilityModel.abilityPoint - ability.skillPointCost) {
         //       player.giveExperiencePoints(-cost);
                abilityModel.increaseAbilityLevel(ability);
                SyncToClient.send(player);
            }
        });
        context.get().setPacketHandled(true);
    }
    private Ability getAbilityByName(String name){
        switch (name){
            case "aid_support": return new AidSupportAbility();
            default: throw new IllegalArgumentException("Invalid ability name");
        }
    }
}
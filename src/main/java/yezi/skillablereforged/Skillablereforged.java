package yezi.skillablereforged;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import yezi.skillablereforged.client.Keybind;
import yezi.skillablereforged.client.Overlay;
import yezi.skillablereforged.client.Tooltips;
import yezi.skillablereforged.common.CuriosCompat;
import yezi.skillablereforged.common.EventHandler;
import yezi.skillablereforged.common.capabilities.AbilityModel;
import yezi.skillablereforged.common.capabilities.SkillModel;
import yezi.skillablereforged.common.commands.Commands;
import yezi.skillablereforged.common.network.*;
import yezi.skillablereforged.event.ClientEvents;

import java.util.Optional;


// The value here should match an entry in the META-INF/mods.toml file
@Mod("skillablereforged")

public class Skillablereforged
{
    public static final String MOD_ID = "skillablereforged";
    public static SimpleChannel NETWORK;

    public Skillablereforged() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::initCaps);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.getConfig());
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        Config.load();
        NETWORK = NetworkRegistry.newSimpleChannel(new ResourceLocation("skillablereforged", "main_channel"), () -> "1.0", (s) -> true, (s) -> true);
        NETWORK.registerMessage(1, SyncToClient.class, SyncToClient::encode, SyncToClient::new, SyncToClient::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        NETWORK.registerMessage(2, AbilitySyncToClient.class, AbilitySyncToClient::encode, AbilitySyncToClient::new, AbilitySyncToClient::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        NETWORK.registerMessage(3, RequestGetAbility.class, RequestGetAbility::encode, RequestGetAbility::new, RequestGetAbility::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        NETWORK.registerMessage(4, RequestLevelUp.class, RequestLevelUp::encode, RequestLevelUp::new, RequestLevelUp::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        NETWORK.registerMessage(5, NotifyWarning.class, NotifyWarning::encode, NotifyWarning::new, NotifyWarning::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        NETWORK.registerMessage(6, SyncSkillConfigPacket.class, SyncSkillConfigPacket::toBytes, SyncSkillConfigPacket::new, SyncSkillConfigPacket::handle);
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    //    MinecraftForge.EVENT_BUS.register(new GrazieryPassive0Listener());
        MinecraftForge.EVENT_BUS.register(new Commands());
        if (ModList.get().isLoaded("curios")) {
            MinecraftForge.EVENT_BUS.register(new CuriosCompat());
        }

    }

    public void initCaps(RegisterCapabilitiesEvent event) {
        event.register(SkillModel.class);
        event.register(AbilityModel.class);
    }

    private void clientSetup(FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new Tooltips());
        MinecraftForge.EVENT_BUS.register(new Overlay());
        MinecraftForge.EVENT_BUS.register(new Keybind());
        MinecraftForge.EVENT_BUS.register(ClientEvents.class);
    }
}

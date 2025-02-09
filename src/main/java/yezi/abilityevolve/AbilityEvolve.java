package yezi.abilityevolve;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
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
import org.slf4j.Logger;
import org.spongepowered.asm.launch.MixinBootstrap;
import yezi.abilityevolve.client.Keybind;
import yezi.abilityevolve.client.Overlay;
import yezi.abilityevolve.client.Tooltips;
import yezi.abilityevolve.common.CuriosCompat;
import yezi.abilityevolve.common.capabilities.AbilityModel;
import yezi.abilityevolve.common.capabilities.SkillModel;
import yezi.abilityevolve.common.commands.Commands;
import yezi.abilityevolve.common.network.*;
import yezi.abilityevolve.common.utils.ParticleSpawner;
import yezi.abilityevolve.common.listener.ClientEvents;

import java.util.Optional;


// The value here should match an entry in the META-INF/mods.toml file
@Mod("AbilityEvolve")

public class AbilityEvolve
{
    public static final String MOD_ID = "AbilityEvolve";
    public static final String VERSION = "1.20.1-1.0.0";
    public static SimpleChannel NETWORK;
    public static final Logger LOGGER = LogUtils.getLogger();

    public AbilityEvolve() {
        LOGGER.info("AbilityEvolve Loaded.");
        MixinBootstrap.init();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::initCaps);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.getConfig());
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        Config.load();
        NETWORK = NetworkRegistry.newSimpleChannel(new ResourceLocation(MOD_ID, "main_channel"), () -> VERSION, (s) -> true, (s) -> true);
        NETWORK.registerMessage(1, SyncToClient.class, SyncToClient::encode, SyncToClient::new, SyncToClient::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        NETWORK.registerMessage(2, AbilitySyncToClient.class, AbilitySyncToClient::encode, AbilitySyncToClient::new, AbilitySyncToClient::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        NETWORK.registerMessage(3, RequestGetAbility.class, RequestGetAbility::encode, RequestGetAbility::new, RequestGetAbility::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        NETWORK.registerMessage(4, RequestLevelUp.class, RequestLevelUp::encode, RequestLevelUp::new, RequestLevelUp::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        NETWORK.registerMessage(5, Warning.class, Warning::encode, Warning::new, Warning::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        NETWORK.registerMessage(6, SyncConfigPacket.class, SyncConfigPacket::toBytes, SyncConfigPacket::new, SyncConfigPacket::handle);
        MinecraftForge.EVENT_BUS.register(new Commands());
        MinecraftForge.EVENT_BUS.register(ParticleSpawner.class);
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

package yezi.skillablereforged.common.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class AbilityCapability {
    public static final Capability<AbilityModel> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {
    });
}

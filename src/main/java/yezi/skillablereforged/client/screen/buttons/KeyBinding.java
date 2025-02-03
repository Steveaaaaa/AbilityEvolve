package yezi.skillablereforged.client.screen.buttons;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;

public final class KeyBinding {
    public static final String KEY_CATEGORY = "key.category.skillablereforged";
    public static final String OPEN_SKILLS_KEY = "key.skillablereforged.open_skills";
    public static final KeyMapping SKILLS_KEY;

    public KeyBinding() {
    }

    static {
        SKILLS_KEY = new KeyMapping("key.skillablereforged.open_skills", KeyConflictContext.IN_GAME, InputConstants.getKey(71, -1), "key.category.skillablereforged");
    }
}

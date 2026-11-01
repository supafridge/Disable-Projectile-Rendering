package gg.fridge.Disable_Projectile_Rendering;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumMap;
import java.util.Map;

import gg.fridge.Disable_Projectile_Rendering.Configuration.ModConfiguration;
import gg.fridge.Disable_Projectile_Rendering.Enums.ProjectileCategory;

public class ModEntry implements ClientModInitializer {
    public static final String MOD_ID = "disable_projectile_rendering";
    public static final Logger LOGGER = LoggerFactory.getLogger("Disable Projectile Rendering");

    private static KeyMapping masterRenderingKeybind;
    private static final Map<ProjectileCategory, KeyMapping> CATEGORY_KEYS =
            new EnumMap<>(ProjectileCategory.class);

    @Override
    public void onInitializeClient() {
        LOGGER.info("[Disable Projectile Rendering]: Hello, world!");

        // Load configuration
        ModConfiguration.load();

        // Create keybinds
        KeyMapping.Category category = KeyMapping.Category.register(
                Identifier.fromNamespaceAndPath("disable_projectile_rendering", "main"));

        // Master projectile rendering toggle
        masterRenderingKeybind = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.disable_projectile_rendering.toggle", InputConstants.KEY_P, category));

        // One toggle keybind per category, unbound by default
        for (ProjectileCategory projectileCategory : ProjectileCategory.values()) {
            CATEGORY_KEYS.put(projectileCategory, KeyMappingHelper.registerKeyMapping(
                    new KeyMapping(projectileCategory.keyMappingName(), InputConstants.UNKNOWN.getValue(), category)));
        }

        // Adds the master toggle keybind and keybind for each category
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (masterRenderingKeybind.consumeClick()) {
                ProjectileFilter.hideAll = !ProjectileFilter.hideAll;
                ModConfiguration.save();
                showRenderingStatusMessage(client, Component.translatable("disable_projectile_rendering.category.all"),
                        ProjectileFilter.hideAll, false);
            }

            // Create keybinds for each projectile category
            for (Map.Entry<ProjectileCategory, KeyMapping> entry : CATEGORY_KEYS.entrySet()) {
                while (entry.getValue().consumeClick()) {
                    boolean hidden = entry.getKey().toggle();
                    ModConfiguration.save();
                    showRenderingStatusMessage(client, entry.getKey().label(), hidden, true);
                }
            }
        });
    }

    /** Shows a message on the player's actionbar. */
    private static void showRenderingStatusMessage(Minecraft client, Component label, boolean hidden, boolean isCategory) {
        if (client.player == null || !ProjectileFilter.showStatusMessage) return;

        // Check if it's being overridden
        boolean overridden = isCategory && ProjectileFilter.hideAll;
        String key = "disable_projectile_rendering.status."
                + (hidden ? "disabled" : "enabled")
                + (overridden ? "_overridden" : "");

        Component message = Component.translatable(key, label)
                .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC);

        client.gui.hud.setOverlayMessage(message, false);
    }
}

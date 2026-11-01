package gg.fridge.Disable_Projectile_Rendering.Configuration;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import net.caffeinemc.mods.sodium.api.config.ConfigEntryPoint;
import net.caffeinemc.mods.sodium.api.config.structure.ConfigBuilder;

import java.util.Locale;

import gg.fridge.Disable_Projectile_Rendering.Enums.ProjectileCategory;
import gg.fridge.Disable_Projectile_Rendering.Enums.ProjectileType;
import gg.fridge.Disable_Projectile_Rendering.ModEntry;
import gg.fridge.Disable_Projectile_Rendering.ProjectileFilter;

/** Class representing the mod's options page. */
public class ModOptions implements ConfigEntryPoint {
    private static final String MOD_ID = ModEntry.MOD_ID;

    @Override
    public void registerConfigLate(ConfigBuilder builder) {
        var page = builder.createOptionPage()
                .setName(Component.translatable("disable_projectile_rendering.configuration.title"));

        // Master switch
        var general = builder.createOptionGroup()
                .setName(Component.translatable("disable_projectile_rendering.configuration.general"));
        general.addOption(builder.createBooleanOption(Identifier.parse(MOD_ID + ":hide_all"))
                .setName(Component.translatable("disable_projectile_rendering.configuration.master_switch"))
                .setTooltip(Component.translatable("disable_projectile_rendering.configuration.master_switch.tooltip"))
                .setStorageHandler(ModConfiguration::save)
                .setBinding(v -> ProjectileFilter.hideAll = v, () -> ProjectileFilter.hideAll)
                .setDefaultValue(ProjectileFilter.hideAll));
        general.addOption(builder.createBooleanOption(Identifier.parse(MOD_ID + ":show_status_messages"))
                .setName(Component.translatable("disable_projectile_rendering.configuration.show_status_messages"))
                .setTooltip(Component.translatable("disable_projectile_rendering.configuration.show_status_messages.tooltip"))
                .setStorageHandler(ModConfiguration::save)
                .setBinding(v -> ProjectileFilter.showStatusMessage = v, () -> ProjectileFilter.showStatusMessage)
                .setDefaultValue(true));
        page.addOptionGroup(general);

        // One group per section, one checkbox per projectile type
        for (ProjectileCategory category : ProjectileCategory.values()) {
            var group = builder.createOptionGroup()
                    .setName(category.groupTitle());

            // Create a boolean option for each category type
            for (ProjectileType type : category.types()) {
                group.addOption(builder.createBooleanOption(
                        Identifier.parse(MOD_ID + ":" + type.name().toLowerCase(Locale.ROOT)))
                        .setName(Component.translatable("disable_projectile_rendering.configuration.projectile_type", makePretty(type)))
                        .setTooltip(Component.translatable("disable_projectile_rendering.configuration.projectile_type.tooltip"))
                        .setStorageHandler(ModConfiguration::save)
                        .setBinding(v -> ProjectileFilter.set(v, type), () -> ProjectileFilter.isHidden(type))
                        .setDefaultValue(ProjectileFilter.isHidden(type)));
            }
            page.addOptionGroup(group);
        }

        builder.registerOwnModOptions()
                .setIcon(Identifier.parse(MOD_ID + ":icon.png"))
                .addPage(page);
    }

    /** Turns each projectile type into a better, human-legible name. */
    private static String makePretty(ProjectileType type) {
        StringBuilder output = new StringBuilder();
        for (String word : type.name().toLowerCase(Locale.ROOT).split("_")) {
            if (!output.isEmpty()) output.append(" ");
            output.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
        }
        return output.toString();
    }
}

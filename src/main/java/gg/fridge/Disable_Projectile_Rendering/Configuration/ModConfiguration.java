package gg.fridge.Disable_Projectile_Rendering.Configuration;

import net.fabricmc.loader.api.FabricLoader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.slf4j.Logger;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import gg.fridge.Disable_Projectile_Rendering.Enums.ProjectileType;
import gg.fridge.Disable_Projectile_Rendering.ModEntry;
import gg.fridge.Disable_Projectile_Rendering.ProjectileFilter;

public final class ModConfiguration {
    private static final Logger LOGGER = ModEntry.LOGGER;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance().getConfigDir()
            .resolve("Disable_Projectile_Rendering.json");

    private static final String HIDE_ALL_KEY = "hide_all";
    private static final String SHOW_STATUS_MESSAGES_KEY = "show_status_messages";

    private ModConfiguration() {}

    /** Loads the mod's configuration file. */
    public static void load() {
        if (!Files.exists(FILE)) {
            save();
            return;
        }

        try (Reader reader = Files.newBufferedReader(FILE)) {
            Map<String, Boolean> saved = GSON.fromJson(reader,
                    new TypeToken<Map<String, Boolean>>() {}.getType());
            if (saved == null) return;

            Boolean hideAll = saved.get(HIDE_ALL_KEY);
            if (hideAll != null) ProjectileFilter.hideAll = hideAll;
            Boolean showStatusMessages = saved.get(SHOW_STATUS_MESSAGES_KEY);
            if (showStatusMessages != null) ProjectileFilter.showStatusMessage = showStatusMessages;

            for (ProjectileType type : ProjectileType.values()) {
                Boolean hide = saved.get(keyOf(type));
                if (hide != null) ProjectileFilter.set(hide, type);
            }
        } catch (Exception exception) {
            LOGGER.error("[Disable Projectile Rendering]: Failed to read configuration, keeping current values",
                    exception);
        }
    }

    /** Saves the mod's configuration file. */
    public static void save() {
        Map<String, Boolean> output = new LinkedHashMap<>();
        output.put(HIDE_ALL_KEY, ProjectileFilter.hideAll);
        output.put(SHOW_STATUS_MESSAGES_KEY, ProjectileFilter.showStatusMessage);
        for (ProjectileType type : ProjectileType.values()) {
            output.put(keyOf(type), ProjectileFilter.isHidden(type));
        }

        try (Writer writer = Files.newBufferedWriter(FILE)) {
            GSON.toJson(output, writer);
        } catch (Exception exception) {
            LOGGER.error("Failed to save configuration", exception);
        }
    }

    /** Gets the namespaced component-usable name for the projectile. */
    private static String keyOf(ProjectileType type) {
        return type.name().toLowerCase(Locale.ROOT);
    }
}

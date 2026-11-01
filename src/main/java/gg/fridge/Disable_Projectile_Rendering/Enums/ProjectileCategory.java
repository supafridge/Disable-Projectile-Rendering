package gg.fridge.Disable_Projectile_Rendering.Enums;

import net.minecraft.network.chat.Component;

import java.util.Locale;

import gg.fridge.Disable_Projectile_Rendering.ProjectileFilter;

/** Enum representing a category/type of projectiles. */
public enum ProjectileCategory {
    ARROWS("Arrows", ProjectileType.ARROW, ProjectileType.SPECTRAL_ARROW, ProjectileType.TRIDENT),
    THROWN_ITEMS("Thrown Items", ProjectileType.SNOWBALL, ProjectileType.EGG, ProjectileType.ENDER_PEARL, ProjectileType.EXPERIENCE_BOTTLE),
    POTIONS("Potions", ProjectileType.SPLASH_POTION, ProjectileType.LINGERING_POTION),
    FIREBALLS("Fireballs", ProjectileType.FIREBALL, ProjectileType.SMALL_FIREBALL, ProjectileType.WITHER_SKULL, ProjectileType.DRAGON_FIREBALL),
    WIND_CHARGES("Wind Charges", ProjectileType.WIND_CHARGE, ProjectileType.BREEZE_WIND_CHARGE),
    MOB_ATTACKS("Mob Attacks", ProjectileType.SHULKER_BULLET, ProjectileType.LLAMA_SPIT),
    UTILITY("Utility", ProjectileType.FISHING_BOBBER, ProjectileType.FIREWORK_ROCKET),
    OTHER("Other", ProjectileType.OTHER);

    private final String displayName;
    private final ProjectileType[] types;

    ProjectileCategory(String displayName, ProjectileType... types) {
        this.displayName = displayName;
        this.types = types;
    }

    public String displayName() { return this.displayName; }
    public ProjectileType[] types() { return this.types; }

    /** Gets the designated translation key for the projectile category. */
    public String translationKey() {
        return "disable_projectile_rendering.category." + name().toLowerCase(Locale.ROOT);
    }

    /** Gets the translated component for the projectile category. */
    public Component label() {
        return Component.translatable(translationKey());
    }

    public Component groupTitle() {
        return Component.translatable(
                "disable_projectile_rendering.options.group." + name().toLowerCase(Locale.ROOT));
    }

    /** Gets the translated name of the key mapping for the projectile category. */
    public String keyMappingName() {
        return "key.disable_projectile_rendering.toggle_" + name().toLowerCase(Locale.ROOT);
    }

    /** Checks if all projectiles in this category are hidden. */
    public boolean areAllProjectilesHidden() {
        for (ProjectileType type : this.types) {
            if (!ProjectileFilter.isHidden(type)) return false;
        }
        return true;
    }

    /** Sets each projectile in this category to be hidden from the renderer. */
    public void setHidden(boolean hide) {
        ProjectileFilter.set(hide, types);
    }

    /** Toggles the rendering state of all projectile keybinds in this category. */
    public boolean toggle() {
        boolean hide = !areAllProjectilesHidden();
        setHidden(hide);
        return hide;
    }
}

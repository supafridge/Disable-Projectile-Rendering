package gg.fridge.Disable_Projectile_Rendering;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;

import java.util.IdentityHashMap;
import java.util.Map;

import gg.fridge.Disable_Projectile_Rendering.Enums.ProjectileType;

public final class ProjectileFilter {
    /** Master switch for projectile rendering */
    public static boolean hideAll = false;
    public static boolean showStatusMessage = true;

    // true = hide, false = show
    private static boolean hideArrow                = false;
    private static boolean hideSpectralArrow        = false;
    private static boolean hideTrident              = false;
    private static boolean hideSnowball             = false;
    private static boolean hideEgg                  = false;
    private static boolean hideEnderPearl           = false;
    private static boolean hideExperienceBottle     = false;
    private static boolean hideSplashPotion         = false;
    private static boolean hideLingeringPotion      = false;
    private static boolean hideFireball             = false;
    private static boolean hideSmallFireball        = false;
    private static boolean hideWitherSkull          = false;
    private static boolean hideDragonFireball       = false;
    private static boolean hideWindCharge           = false;
    private static boolean hideBreezeWindCharge     = false;
    private static boolean hideShulkerBullet        = false;
    private static boolean hideLlamaSpit            = false;
    private static boolean hideFishingBobber        = false;
    private static boolean hideFireworkRocket       = false;
    private static boolean hideOther                = false;

    private static final Map<EntityType<?>, String> ID_CACHE = new IdentityHashMap<>();

    private ProjectileFilter() {}

    /** Changes the projectiles given to be rendered/not rendered. */
    public static void set(boolean hide, ProjectileType... types) {
        for (ProjectileType type : types) {
            switch (type) {
                case ARROW              -> hideArrow = hide;
                case SPECTRAL_ARROW     -> hideSpectralArrow = hide;
                case TRIDENT            -> hideTrident = hide;
                case SNOWBALL           -> hideSnowball = hide;
                case EGG                -> hideEgg = hide;
                case ENDER_PEARL        -> hideEnderPearl = hide;
                case EXPERIENCE_BOTTLE  -> hideExperienceBottle = hide;
                case SPLASH_POTION      -> hideSplashPotion = hide;
                case LINGERING_POTION   -> hideLingeringPotion = hide;
                case FIREBALL           -> hideFireball = hide;
                case SMALL_FIREBALL     -> hideSmallFireball = hide;
                case WITHER_SKULL       -> hideWitherSkull = hide;
                case DRAGON_FIREBALL    -> hideDragonFireball = hide;
                case WIND_CHARGE        -> hideWindCharge = hide;
                case BREEZE_WIND_CHARGE -> hideBreezeWindCharge = hide;
                case SHULKER_BULLET     -> hideShulkerBullet = hide;
                case LLAMA_SPIT         -> hideLlamaSpit = hide;
                case FISHING_BOBBER     -> hideFishingBobber = hide;
                case FIREWORK_ROCKET    -> hideFireworkRocket = hide;
                case OTHER              -> hideOther = hide;
            }
        }
    }

    /** Sets all projectiles to be rendered/not rendered. */
    public static void setAll(boolean hide) {
        set(hide, ProjectileType.values());
    }

    /** Checks if a specific projectile is hidden or not from the renderer. */
    public static boolean isHidden(ProjectileType type) {
        return switch (type) {
            case ARROW              -> hideArrow;
            case SPECTRAL_ARROW     -> hideSpectralArrow;
            case TRIDENT            -> hideTrident;
            case SNOWBALL           -> hideSnowball;
            case EGG                -> hideEgg;
            case ENDER_PEARL        -> hideEnderPearl;
            case EXPERIENCE_BOTTLE  -> hideExperienceBottle;
            case SPLASH_POTION      -> hideSplashPotion;
            case LINGERING_POTION   -> hideLingeringPotion;
            case FIREBALL           -> hideFireball;
            case SMALL_FIREBALL     -> hideSmallFireball;
            case WITHER_SKULL       -> hideWitherSkull;
            case DRAGON_FIREBALL    -> hideDragonFireball;
            case WIND_CHARGE        -> hideWindCharge;
            case BREEZE_WIND_CHARGE -> hideBreezeWindCharge;
            case SHULKER_BULLET     -> hideShulkerBullet;
            case LLAMA_SPIT         -> hideLlamaSpit;
            case FISHING_BOBBER     -> hideFishingBobber;
            case FIREWORK_ROCKET    -> hideFireworkRocket;
            case OTHER              -> hideOther;
        };
    }

    /** Checks if a specific projectile should be hidden from the renderer. */
    public static boolean shouldHide(Entity entity) {
        if (!(entity instanceof Projectile)) return false;
        if (hideAll) return true;
        String identifier = ID_CACHE.computeIfAbsent(entity.getType(),
                type -> EntityType.getKey(type).getPath());
        return getProjectileTypeFromString(identifier);
    }

    /** Returns the variable of the projectile type from its name. */
    public static boolean getProjectileTypeFromString(String type) {
        return switch (type) {
            case "arrow" -> hideArrow;
            case "spectral_arrow" -> hideSpectralArrow;
            case "trident" -> hideTrident;
            case "snowball" -> hideSnowball;
            case "egg" -> hideEgg;
            case "ender_pearl" -> hideEnderPearl;
            case "experience_bottle" -> hideExperienceBottle;
            case "potion", "lingering_potion" -> hideLingeringPotion;
            case "fireball" -> hideFireball;
            case "small_fireball" -> hideSmallFireball;
            case "wither_skull" -> hideWitherSkull;
            case "dragon_fireball" -> hideDragonFireball;
            case "wind_charge" -> hideWindCharge;
            case "breeze_wind_charge" -> hideBreezeWindCharge;
            case "shulker_bullet" -> hideShulkerBullet;
            case "llama_spit" -> hideLlamaSpit;
            case "fishing_bobber" -> hideFishingBobber;
            case "firework_rocket" -> hideFireworkRocket;
            default -> hideOther;
        };
    }
}

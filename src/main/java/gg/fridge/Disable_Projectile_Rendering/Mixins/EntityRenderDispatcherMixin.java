package gg.fridge.Disable_Projectile_Rendering.Mixins;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.Mixin;

import gg.fridge.Disable_Projectile_Rendering.ProjectileFilter;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    private void DisableProjectileRendering$disableProjectileRendering(Entity entity, Frustum culler,
                                                                       double camX, double camY, double camZ,
                                                                       CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (ProjectileFilter.shouldHide(entity))
            callbackInfoReturnable.setReturnValue(false);
    }
}

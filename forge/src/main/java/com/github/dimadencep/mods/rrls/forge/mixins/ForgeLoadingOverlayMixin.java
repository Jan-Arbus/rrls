/*
 * Copyright 2023 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://github.com/dima-dencep/rrls/blob/HEAD/LICENSE
 */

package com.github.dimadencep.mods.rrls.forge.mixins;

import com.github.dimadencep.mods.rrls.Rrls;
import com.github.dimadencep.mods.rrls.accessor.SplashAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.resource.ResourceReload;
import net.neoforged.fml.earlydisplay.DisplayWindow;
import net.neoforged.fml.loading.progress.ProgressMeter;
import net.neoforged.neoforge.client.loading.NeoForgeLoadingOverlay;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.function.Consumer;

@Mixin(NeoForgeLoadingOverlay.class)
public abstract class ForgeLoadingOverlayMixin extends SplashOverlay {
    @Shadow
    @Final
    private MinecraftClient minecraft;
    @Shadow
    @Final
    private ProgressMeter progressMeter;
    @Shadow
    @Final
    private DisplayWindow displayWindow;
    @Shadow
    private float currentProgress;

    public ForgeLoadingOverlayMixin(MinecraftClient client, ResourceReload monitor, Consumer<Optional<Throwable>> exceptionHandler, boolean reloading) {
        super(client, monitor, exceptionHandler, reloading);
    }

    @Override
    public void rrls$endhook() {
        progressMeter.complete();
        displayWindow.close();
    }

    @Override
    public void rrls$progress(float progress) {
        currentProgress = progress;
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    public void rrls$render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        rrls$setAttachType(rrls$filterAttachType(minecraft.currentScreen, rrls$getAttachType() != AttachType.WAIT));

        if (rrls$getAttachType() == SplashAccessor.AttachType.HIDE) {
            ci.cancel();
        }
    }

    @ModifyConstant(
            method = "render",
            constant = @Constant(
                    floatValue = 1000.0F,
                    ordinal = 0
            ),
            require = 0
    )
    public float rrls$changeAnimationSpeed(float instance) {
        return Rrls.MOD_CONFIG.animationSpeed;
    }
}

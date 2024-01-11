/*
 * Copyright 2023 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://github.com/dima-dencep/rrls/blob/HEAD/LICENSE
 */

package com.github.dimadencep.mods.rrls.mixins.compat;

import com.github.dimadencep.mods.rrls.Rrls;
import net.minecraft.client.resource.server.PackStateChangeCallback;
import net.minecraft.client.resource.server.ServerResourcePackManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(ServerResourcePackManager.class)
public class ServerResourcePackManagerMixin {
    @Shadow
    @Final
    PackStateChangeCallback stateChangeCallback;

    @Inject(
            method = "onAdd",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/resource/server/ServerResourcePackManager;onPackChanged()V"
            )
    )
    public void earlyResourcePackStatusSend(UUID id, ServerResourcePackManager.PackEntry pack, CallbackInfo ci) {
        if (Rrls.MOD_CONFIG.earlyPackStatusSend) {
            this.stateChangeCallback.onFinish(pack.id, PackStateChangeCallback.FinishState.APPLIED);
        }
    }
}

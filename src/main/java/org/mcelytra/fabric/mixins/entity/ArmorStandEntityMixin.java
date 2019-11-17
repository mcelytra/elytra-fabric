/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of mcelytra.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.mcelytra.fabric.mixins.entity;

import net.minecraft.entity.decoration.ArmorStandEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Represents a mixin to add automatically arms to every spawned armor stands.
 */
@Mixin(ArmorStandEntity.class)
public abstract class ArmorStandEntityMixin
{
    @Shadow
    protected abstract void setShowArms(boolean boolean_1);

    @Inject(method = "initDataTracker", at = @At("RETURN"))
    protected void initDataTracker(CallbackInfo ci)
    {
        this.setShowArms(true);
    }
}

/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of mcelytra.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.mcelytra.fabric.mixins.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.world.World;
import org.mcelytra.core.entity.EntityArmorStand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Represents a mixin to add automatically arms to every spawned armor stands.
 */
// @TODO Add intelligent interactions with armor stands to remove base plate and arms.
@Mixin(ArmorStandEntity.class)
public abstract class ArmorStandEntityMixin extends LivingEntity implements EntityArmorStand
{
    protected ArmorStandEntityMixin(EntityType<? extends LivingEntity> type, World world)
    {
        super(type, world);
    }

    @Shadow
    protected abstract void setShowArms(boolean boolean_1);

    @Shadow
    public abstract boolean isSmall();

    @Shadow
    protected abstract void setSmall(boolean bl);

    @Shadow
    public abstract boolean shouldShowArms();

    @Shadow
    public abstract boolean shouldHideBasePlate();

    @Shadow
    protected abstract void setHideBasePlate(boolean bl);

    @Shadow
    public abstract boolean isMarker();

    @Shadow
    protected abstract void setMarker(boolean bl);

    @Inject(method = "initDataTracker", at = @At("RETURN"))
    protected void initDataTracker(CallbackInfo ci)
    {
        this.set_arms(true);
    }

    @Override
    public boolean is_small()
    {
        return this.isSmall();
    }

    @Override
    public void set_small(boolean small)
    {
        this.setSmall(small);
    }

    @Override
    public boolean has_arms()
    {
        return this.shouldShowArms();
    }

    @Override
    public void set_arms(boolean arms)
    {
        this.setShowArms(arms);
    }

    @Override
    public boolean should_hide_base_plate()
    {
        return this.shouldHideBasePlate();
    }

    @Override
    public void set_hide_base_plate(boolean hide_base_plate)
    {
        this.setHideBasePlate(hide_base_plate);
    }

    @Override
    public boolean is_marker()
    {
        return this.isMarker();
    }

    @Override
    public void set_marker(boolean marker)
    {
        this.setMarker(marker);
    }
}

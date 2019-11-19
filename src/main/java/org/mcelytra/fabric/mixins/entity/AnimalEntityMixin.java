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
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.mcelytra.core.entity.passive.EntityAnimal;
import org.mcelytra.core.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AnimalEntity.class)
public abstract class AnimalEntityMixin extends PassiveEntity implements EntityAnimal
{
    @Shadow
    public abstract boolean isInLove();

    @Shadow
    private int loveTicks;

    @Shadow
    public abstract void resetLoveTicks();

    @Shadow
    public abstract boolean isBreedingItem(ItemStack stack);

    protected AnimalEntityMixin(EntityType<? extends PassiveEntity> type, World world)
    {
        super(type, world);
    }

    @Override
    public boolean is_breeding_item(Item item)
    {
        return this.isBreedingItem(new net.minecraft.item.ItemStack((ItemConvertible) item));
    }

    @Override
    public boolean is_in_love()
    {
        return this.isInLove();
    }

    @Override
    public int get_love_ticks()
    {
        return this.loveTicks;
    }

    @Override
    public void reset_love_ticks()
    {
        this.resetLoveTicks();
    }
}

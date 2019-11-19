/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of mcelytra.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.mcelytra.fabric.mixins.item;

import org.mcelytra.core.inventory.ItemStack;
import org.mcelytra.fabric.inventory.ElytraItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(net.minecraft.item.ItemStack.class)
public abstract class ItemStackMixin implements ElytraItemStack.Implementation
{
    @Override
    public ItemStack as_eltyra()
    {
        return new ElytraItemStack((net.minecraft.item.ItemStack) (Object) this);
    }
}

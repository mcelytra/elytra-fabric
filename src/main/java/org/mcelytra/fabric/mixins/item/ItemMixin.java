/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of mcelytra.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.mcelytra.fabric.mixins.item;

import org.mcelytra.core.item.Item;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(net.minecraft.item.Item.class)
public abstract class ItemMixin implements Item
{

}

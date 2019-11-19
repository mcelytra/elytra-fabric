/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of mcelytra.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.mcelytra.fabric.mixins;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import org.mcelytra.core.Elytra;
import org.mcelytra.core.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DefaultedRegistry.class)
public class DefaultedRegistryMixin extends SimpleRegistry
{
    @Inject(method = "set", at = @At("RETURN"))
    private <V> void on_set(int raw_id, Identifier id, V entry, CallbackInfoReturnable<V> cir)
    {
        if (((Object) this) instanceof DefaultedRegistry)
            if ((Object) this == Registry.ITEM) {
                Elytra.ITEM_REGISTRY.add(new org.aperlambda.lambdacommon.Identifier(id.toString()), (Item) entry);
            }
    }
}

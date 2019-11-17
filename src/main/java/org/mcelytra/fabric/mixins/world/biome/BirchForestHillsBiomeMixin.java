/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of mcelytra.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.mcelytra.fabric.mixins.world.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BirchForestHillsBiome;
import org.mcelytra.fabric.utils.GenerationUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BirchForestHillsBiome.class)
public abstract class BirchForestHillsBiomeMixin extends Biome
{
    protected BirchForestHillsBiomeMixin(Settings settings)
    {
        super(settings);
    }

    @Inject(method = "<init>*", at = @At("RETURN"))
    private void on_new(CallbackInfo ci)
    {
        GenerationUtils.add_fallen_birch_trees(this);
    }
}

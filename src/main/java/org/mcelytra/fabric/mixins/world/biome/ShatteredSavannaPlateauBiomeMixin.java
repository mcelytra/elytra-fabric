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
import net.minecraft.world.biome.ShatteredSavannaPlateauBiome;
import org.mcelytra.fabric.utils.GenerationUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShatteredSavannaPlateauBiome.class)
public abstract class ShatteredSavannaPlateauBiomeMixin extends Biome
{
    protected ShatteredSavannaPlateauBiomeMixin(Settings settings)
    {
        super(settings);
    }

    @Inject(method = "<init>*", at = @At("RETURN"))
    private void on_new(CallbackInfo ci)
    {
        GenerationUtils.add_savanna_fallen_trees(this);
        GenerationUtils.add_savanna_bushes(this, 0.3f);
    }
}

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
import net.minecraft.world.biome.SwampBiome;
import org.mcelytra.fabric.utils.GenerationUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SwampBiome.class)
public abstract class SwampBiomeMixin extends Biome
{
    protected SwampBiomeMixin(Settings biome$Settings_1)
    {
        super(biome$Settings_1);
    }

    @Inject(method = "<init>*", at = @At("RETURN"))
    private void on_new_swamp_biome(CallbackInfo ci)
    {
        GenerationUtils.add_swamp_mushroom_fields_features(this);
    }
}

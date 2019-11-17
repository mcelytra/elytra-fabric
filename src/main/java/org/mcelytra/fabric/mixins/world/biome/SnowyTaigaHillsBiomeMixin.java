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
import net.minecraft.world.biome.SnowyTaigaHillsBiome;
import org.mcelytra.fabric.utils.GenerationUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SnowyTaigaHillsBiome.class)
public abstract class SnowyTaigaHillsBiomeMixin extends Biome
{
    protected SnowyTaigaHillsBiomeMixin(Settings settings)
    {
        super(settings);
    }

    @Redirect(method = "<init>*", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/DefaultBiomeFeatures;addTaigaTrees(Lnet/minecraft/world/biome/Biome;)V"))
    private void on_new(Biome biome)
    {
        GenerationUtils.add_taiga_trees(biome);
    }
}

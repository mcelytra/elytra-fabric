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
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.biome.SavannaBiome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.CountDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.Feature;
import org.mcelytra.fabric.utils.GenerationUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SavannaBiome.class)
public abstract class SavannaBiomeMixin extends Biome
{
    protected SavannaBiomeMixin(Settings settings)
    {
        super(settings);
    }

    @Inject(method = "<init>*", at = @At("RETURN"))
    private void on_new(CallbackInfo ci)
    {
        GenerationUtils.add_savanna_fallen_trees(this);
        GenerationUtils.add_savanna_bushes(this, 0.4f);
    }

    @Redirect(method = "<init>*", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/DefaultBiomeFeatures;addSavannaTallGrass(Lnet/minecraft/world/biome/Biome;)V"))
    private void on_tallgrass(Biome biome)
    {
        biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.RANDOM_PATCH.configure(DefaultBiomeFeatures.field_21102).createDecoratedFeature(Decorator.COUNT_HEIGHTMAP_32.configure(new CountDecoratorConfig(32))));
    }
}

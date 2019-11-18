/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of mcelytra.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.mcelytra.fabric.mixins.world.dimension;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeSourceType;
import net.minecraft.world.biome.source.VanillaLayeredBiomeSource;
import net.minecraft.world.biome.source.VanillaLayeredBiomeSourceConfig;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.OverworldDimension;
import net.minecraft.world.gen.chunk.*;
import net.minecraft.world.level.LevelGeneratorType;
import org.mcelytra.fabric.ElytraEntrypoint;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OverworldDimension.class)
public abstract class OverworldDimensionMixin extends Dimension
{
    public OverworldDimensionMixin(World world, DimensionType type, float f)
    {
        super(world, type, f);
    }

    @Inject(method = "createChunkGenerator", at = @At("HEAD"), cancellable = true)
    private void on_create_chunk_generator(CallbackInfoReturnable<ChunkGenerator<? extends ChunkGeneratorConfig>> ci)
    {
        // @TODO Allows for custom generator types from addons.
        // @TODO Fix the get generator type.
        LevelGeneratorType gen_type = this.world.getLevelProperties().getGeneratorType();
        ChunkGeneratorType<FloatingIslandsChunkGeneratorConfig, FloatingIslandsChunkGenerator> floating_islands_generator_type = ChunkGeneratorType.FLOATING_ISLANDS;
        BiomeSourceType<VanillaLayeredBiomeSourceConfig, VanillaLayeredBiomeSource> vanilla_biome_source = BiomeSourceType.VANILLA_LAYERED;
        if (gen_type == ElytraEntrypoint.FLOATING_ISLANDS_GENERATOR) {
            FloatingIslandsChunkGeneratorConfig floating_islands_chunk_gen_config = floating_islands_generator_type.createSettings().withCenter(new BlockPos(0, 64, 0));
            VanillaLayeredBiomeSourceConfig vanillaLayeredBiomeSourceConfig2 = vanilla_biome_source.getConfig(this.world.getLevelProperties());
            ci.setReturnValue(floating_islands_generator_type.create(this.world, vanilla_biome_source.applyConfig(vanillaLayeredBiomeSourceConfig2), floating_islands_chunk_gen_config));
            ci.cancel();
        }
    }
}

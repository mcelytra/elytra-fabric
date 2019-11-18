/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of mcelytra.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.mcelytra.fabric.utils;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.biome.layer.*;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.FloatingIslandsChunkGeneratorConfig;
import net.minecraft.world.gen.chunk.OverworldChunkGeneratorConfig;
import net.minecraft.world.gen.decorator.CountChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.CountExtraChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.NoiseHeightmapDecoratorConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.RandomBooleanFeatureConfig;
import net.minecraft.world.gen.feature.RandomFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.stateprovider.SimpleStateProvider;
import net.minecraft.world.level.LevelGeneratorType;
import org.mcelytra.fabric.ElytraRegistry;
import org.mcelytra.fabric.gen.config.FallenTreeFeatureConfig;

public class GenerationUtils
{
    public static final FallenTreeFeatureConfig FALLEN_OAK_TREE_CONFIG          = new FallenTreeFeatureConfig.Builder(
            new SimpleStateProvider(Blocks.OAK_LOG.getDefaultState()), new SimpleStateProvider(Blocks.OAK_LEAVES.getDefaultState()))
            .build();
    public static final FallenTreeFeatureConfig FALLEN_BIRCH_TREE_CONFIG        = new FallenTreeFeatureConfig.Builder(
            new SimpleStateProvider(Blocks.BIRCH_LOG.getDefaultState()), new SimpleStateProvider(Blocks.BIRCH_LEAVES.getDefaultState()))
            .base_height(4).build();
    public static final FallenTreeFeatureConfig FALLEN_SPRUCE_TREE_CONFIG       = new FallenTreeFeatureConfig.Builder(
            new SimpleStateProvider(Blocks.SPRUCE_LOG.getDefaultState()), new SimpleStateProvider(Blocks.SPRUCE_LEAVES.getDefaultState()))
            .base_height(5).variance(4).build();
    public static final FallenTreeFeatureConfig SNOWY_FALLEN_SPRUCE_TREE_CONFIG = new FallenTreeFeatureConfig.Builder(
            new SimpleStateProvider(Blocks.SPRUCE_LOG.getDefaultState()), new SimpleStateProvider(Blocks.SPRUCE_LEAVES.getDefaultState()))
            .base_height(4).variance(3).snowy(true).mushrooms(false).build();
    public static final FallenTreeFeatureConfig SAVANNA_FALLEN_OAK_TREE_CONFIG  = new FallenTreeFeatureConfig.Builder(
            new SimpleStateProvider(Blocks.OAK_LOG.getDefaultState()), new SimpleStateProvider(Blocks.OAK_LEAVES.getDefaultState()))
            .mushrooms(false).build();
    public static final TreeFeatureConfig       OAK_GROUND_BUSH_CONFIG          = new TreeFeatureConfig.Builder(new SimpleStateProvider(Blocks.OAK_LOG.getDefaultState()), new SimpleStateProvider(Blocks.OAK_LEAVES.getDefaultState())).baseHeight(4).build();
    public static final TreeFeatureConfig       ACACIA_GROUND_BUSH_CONFIG       = new TreeFeatureConfig.Builder(new SimpleStateProvider(Blocks.ACACIA_LOG.getDefaultState()), new SimpleStateProvider(Blocks.ACACIA_LEAVES.getDefaultState())).baseHeight(4).build();

    public static void add_swamp_mushroom_fields_features(Biome biome)
    {
        // The following lines are almost equivalent to DefaultBiomeFeatures.addMushroomFieldsFeatures(this);, the probabilities are changed.
        // This is a parity feature.
        biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.RANDOM_BOOLEAN_SELECTOR.configure(new RandomBooleanFeatureConfig(Feature.HUGE_RED_MUSHROOM.configure(DefaultBiomeFeatures.field_21142), Feature.HUGE_BROWN_MUSHROOM.configure(DefaultBiomeFeatures.field_21143))).createDecoratedFeature(Decorator.COUNT_CHANCE_HEIGHTMAP.configure(new CountChanceDecoratorConfig(1, 0.35F))));
        biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.RANDOM_PATCH.configure(DefaultBiomeFeatures.field_21097).createDecoratedFeature(Decorator.COUNT_CHANCE_HEIGHTMAP.configure(new CountChanceDecoratorConfig(1, 0.25F))));
        biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.RANDOM_PATCH.configure(DefaultBiomeFeatures.field_21096).createDecoratedFeature(Decorator.COUNT_CHANCE_HEIGHTMAP_DOUBLE.configure(new CountChanceDecoratorConfig(1, 0.125F))));
    }

    public static void add_fallen_trees_in_forest(Biome biome)
    {
        biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.RANDOM_BOOLEAN_SELECTOR.configure(new RandomBooleanFeatureConfig(
                ElytraRegistry.FALLEN_TREE.configure(FALLEN_OAK_TREE_CONFIG),
                ElytraRegistry.FALLEN_TREE.configure(FALLEN_BIRCH_TREE_CONFIG)))
                .createDecoratedFeature(Decorator.COUNT_CHANCE_HEIGHTMAP.configure(new CountChanceDecoratorConfig(1, 0.40f))));
    }

    public static void add_fallen_oak_trees(Biome biome)
    {
        biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, ElytraRegistry.FALLEN_TREE.configure(FALLEN_OAK_TREE_CONFIG)
                .createDecoratedFeature(Decorator.COUNT_CHANCE_HEIGHTMAP.configure(new CountChanceDecoratorConfig(1, 0.25f))));
    }

    public static void add_fallen_birch_trees(Biome biome)
    {
        biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, ElytraRegistry.FALLEN_TREE.configure(FALLEN_BIRCH_TREE_CONFIG)
                .createDecoratedFeature(Decorator.COUNT_CHANCE_HEIGHTMAP.configure(new CountChanceDecoratorConfig(1, 0.25f))));
    }

    public static void add_plains_features(Biome biome)
    {
        biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfig(
                ImmutableList.of(
                        Feature.FANCY_TREE.configure(DefaultBiomeFeatures.field_21192).method_23387(0.33333334F),
                        ElytraRegistry.FALLEN_TREE.configure(FALLEN_OAK_TREE_CONFIG).method_23387(0.33333334F)),
                Feature.NORMAL_TREE.configure(DefaultBiomeFeatures.field_21191)))
                .createDecoratedFeature(Decorator.COUNT_EXTRA_HEIGHTMAP.configure(new CountExtraChanceDecoratorConfig(0, 0.05F, 1))));
        biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.FLOWER.configure(DefaultBiomeFeatures.field_21088).createDecoratedFeature(Decorator.NOISE_HEIGHTMAP_32.configure(new NoiseHeightmapDecoratorConfig(-0.8D, 15, 4))));
        biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.RANDOM_PATCH.configure(DefaultBiomeFeatures.field_21201).createDecoratedFeature(Decorator.NOISE_HEIGHTMAP_DOUBLE.configure(new NoiseHeightmapDecoratorConfig(-0.8D, 5, 10))));
    }

    public static void add_taiga_trees(Biome biome)
    {
        biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfig(
                ImmutableList.of(
                        Feature.NORMAL_TREE.configure(DefaultBiomeFeatures.field_21184).method_23387(0.33333334F),
                        ElytraRegistry.FALLEN_TREE.configure(FALLEN_SPRUCE_TREE_CONFIG).method_23387(0.02f)),
                Feature.NORMAL_TREE.configure(DefaultBiomeFeatures.field_21185)))
                .createDecoratedFeature(Decorator.COUNT_EXTRA_HEIGHTMAP.configure(new CountExtraChanceDecoratorConfig(10, 0.1F, 1))));
    }

    public static void add_giant_taiga_trees(Biome biome)
    {
        biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfig(
                ImmutableList.of(
                        Feature.MEGA_SPRUCE_TREE.configure(DefaultBiomeFeatures.field_21198).method_23387(0.025641026F),
                        Feature.MEGA_SPRUCE_TREE.configure(DefaultBiomeFeatures.field_21199).method_23387(0.30769232F),
                        Feature.NORMAL_TREE.configure(DefaultBiomeFeatures.field_21184).method_23387(0.33333334F),
                        ElytraRegistry.FALLEN_TREE.configure(FALLEN_SPRUCE_TREE_CONFIG).method_23387(0.05f)),
                Feature.NORMAL_TREE.configure(DefaultBiomeFeatures.field_21185)))
                .createDecoratedFeature(Decorator.COUNT_EXTRA_HEIGHTMAP.configure(new CountExtraChanceDecoratorConfig(10, 0.1F, 1))));
    }

    public static void add_giant_spruce_taiga_trees(Biome biome)
    {
        biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfig(
                ImmutableList.of(
                        Feature.MEGA_SPRUCE_TREE.configure(DefaultBiomeFeatures.field_21198).method_23387(0.33333334F),
                        Feature.NORMAL_TREE.configure(DefaultBiomeFeatures.field_21184).method_23387(0.33333334F),
                        ElytraRegistry.FALLEN_TREE.configure(FALLEN_SPRUCE_TREE_CONFIG).method_23387(0.05f)),
                Feature.NORMAL_TREE.configure(DefaultBiomeFeatures.field_21185)))
                .createDecoratedFeature(Decorator.COUNT_EXTRA_HEIGHTMAP.configure(new CountExtraChanceDecoratorConfig(10, 0.1F, 1))));
    }

    public static void add_snowy_taiga_trees(Biome biome)
    {
        biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfig(
                ImmutableList.of(
                        Feature.NORMAL_TREE.configure(DefaultBiomeFeatures.field_21184).method_23387(0.33333334F),
                        ElytraRegistry.FALLEN_TREE.configure(SNOWY_FALLEN_SPRUCE_TREE_CONFIG).method_23387(0.05f)),
                Feature.NORMAL_TREE.configure(DefaultBiomeFeatures.field_21185)))
                .createDecoratedFeature(Decorator.COUNT_EXTRA_HEIGHTMAP.configure(new CountExtraChanceDecoratorConfig(10, 0.1F, 1))));
    }

    public static void add_savanna_bushes(Biome biome, float chance)
    {
        biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.RANDOM_BOOLEAN_SELECTOR.configure(new RandomBooleanFeatureConfig(ElytraRegistry.GROUND_BUSH.configure(OAK_GROUND_BUSH_CONFIG), ElytraRegistry.GROUND_BUSH.configure(ACACIA_GROUND_BUSH_CONFIG))).createDecoratedFeature(Decorator.COUNT_CHANCE_HEIGHTMAP.configure(new CountChanceDecoratorConfig(2, chance))));
    }

    public static void add_savanna_fallen_trees(Biome biome)
    {
        biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, ElytraRegistry.FALLEN_TREE.configure(SAVANNA_FALLEN_OAK_TREE_CONFIG)
                .createDecoratedFeature(Decorator.COUNT_CHANCE_HEIGHTMAP.configure(new CountChanceDecoratorConfig(1, 0.1f))));
    }
}

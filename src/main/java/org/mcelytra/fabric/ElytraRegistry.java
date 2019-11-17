/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of mcelytra.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.mcelytra.fabric;

import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.JungleGroundBushFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import org.mcelytra.core.Server;
import org.mcelytra.fabric.gen.FallenTreeFeature;
import org.mcelytra.fabric.gen.config.FallenTreeFeatureConfig;

import java.util.ArrayList;
import java.util.List;

public class ElytraRegistry
{
    private static List<Feature> count = new ArrayList<>();

    public static final Feature<FallenTreeFeatureConfig> FALLEN_TREE              = register(Registry.FEATURE, "fallen_tree", new FallenTreeFeature(FallenTreeFeatureConfig::deserialize));
    public static final Feature<TreeFeatureConfig>       GROUND_BUSH              = register(Registry.FEATURE, "ground_bush", new JungleGroundBushFeature(TreeFeatureConfig::deserialize));

    private static <T extends Feature> T register(Registry<? super T> registry, String id, T entry)
    {
        T res = Registry.register(registry, new Identifier("elytra", id), entry);
        count.add(res);
        return res;
    }

    public static void hello(Server server)
    {
        server.get_logger().info("Registered " + count.size() + " elytra added features.");
    }
}

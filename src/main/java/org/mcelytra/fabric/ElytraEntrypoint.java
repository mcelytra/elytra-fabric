/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of mcelytra.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.mcelytra.fabric;

import net.fabricmc.api.ModInitializer;
import net.minecraft.world.level.LevelGeneratorType;
import org.aperlambda.lambdacommon.utils.LambdaReflection;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;

public class ElytraEntrypoint implements ModInitializer
{
    private static final Constructor<LevelGeneratorType> LEVEL_GENERATOR_TYPE_CONSTRUCTOR = LambdaReflection.get_constructor(LevelGeneratorType.class, int.class, String.class).orElse(null);

    public static final LevelGeneratorType FLOATING_ISLANDS_GENERATOR = register_generator(10, "floating_islands");

    private static LevelGeneratorType register_generator(int id, String name)
    {
        if (LEVEL_GENERATOR_TYPE_CONSTRUCTOR == null)
            return null;
        LEVEL_GENERATOR_TYPE_CONSTRUCTOR.setAccessible(true);
        return LambdaReflection.new_instance(LEVEL_GENERATOR_TYPE_CONSTRUCTOR, id, name);
    }

    @Nullable
    public static LevelGeneratorType get_generator_from_name(String name)
    {
        for (LevelGeneratorType level_generator_type : LevelGeneratorType.TYPES) {
            if (level_generator_type != null && level_generator_type.getName().equalsIgnoreCase(name)) {
                return level_generator_type;
            }
        }

        return null;
    }

    @Override
    public void onInitialize()
    {
        System.out.println("REGISTERED " + FLOATING_ISLANDS_GENERATOR.getName() + "!");
    }
}

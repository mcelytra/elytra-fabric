/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of mcelytra.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.mcelytra.fabric.addon;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import org.jetbrains.annotations.NotNull;
import org.mcelytra.core.addon.DummyAddon;

public abstract class ModAddon extends DummyAddon implements ModInitializer, DedicatedServerModInitializer
{
    @Override
    public void onInitialize()
    {
    }

    @Override
    public void onInitializeServer()
    {
        this.on_enable();
    }

    @Override
    public @NotNull String get_name()
    {
        return null;
    }
}

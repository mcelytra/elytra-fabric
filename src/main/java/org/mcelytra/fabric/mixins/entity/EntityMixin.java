/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of mcelytra.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.mcelytra.fabric.mixins.entity;

import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.mcelytra.core.Server;
import org.mcelytra.core.entity.Entity;
import org.mcelytra.fabric.utils.ConversionUtils;
import org.mcelytra.fabric.utils.ServerGet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

@Mixin(net.minecraft.entity.Entity.class)
public abstract class EntityMixin implements Entity
{
    @Shadow
    public abstract int getEntityId();

    @Shadow
    public abstract UUID getUuid();

    @Shadow
    public abstract String getEntityName();

    @Shadow
    public abstract boolean isSilent();

    @Shadow
    public abstract void setSilent(boolean boolean_1);

    @Shadow
    public abstract boolean hasNoGravity();

    @Shadow
    public abstract void setNoGravity(boolean boolean_1);

    @Shadow
    protected boolean glowing;

    @Shadow
    public abstract boolean isInvisible();

    @Shadow
    public abstract void setInvisible(boolean boolean_1);

    @Shadow
    public abstract void setCustomNameVisible(boolean boolean_1);

    @Shadow
    public abstract boolean isCustomNameVisible();

    @Shadow
    public abstract void setCustomName(@Nullable Text text_1);

    @Shadow
    public abstract boolean hasCustomName();

    @Shadow
    public abstract Text getDisplayName();

    @Shadow
    @Nullable
    public abstract Text getCustomName();

    @Shadow
    private boolean invulnerable;

    @Shadow
    public int age;

    @Shadow
    public abstract boolean isAlive();

    @Shadow
    public abstract void kill();

    @Shadow(prefix = "mc$") public abstract void mc$remove();

    @Shadow private int fireTicks;

    @Shadow protected abstract boolean isBeingRainedOn();

    @Shadow protected abstract boolean isInsideBubbleColumn();

    @Shadow public abstract boolean isInWater();

    @Shadow public abstract boolean isInsideWater();

    @Shadow public boolean onGround;

    @Shadow public abstract boolean isOnFire();

    @Shadow public abstract boolean isFireImmune();

    @Shadow public abstract Iterable<ItemStack> getItemsHand();

    @Shadow public World world;

    @Shadow public abstract EntityType<?> getType();

    @Shadow @Nullable public abstract MinecraftServer getServer();

    @Override
    public int get_entity_id()
    {
        return this.getEntityId();
    }

    @Override
    public int get_ticks_lived()
    {
        return this.age;
    }

    @Override
    public void set_ticks_lived(int value)
    {
        this.age = Math.max(value, 1);
    }

    @Override
    public void remove()
    {
        this.mc$remove();
    }

    @Override
    public boolean is_alive()
    {
        return this.isAlive();
    }

    @Override
    public @NotNull UUID get_unique_id()
    {
        return this.getUuid();
    }

    @Override
    public @NotNull String get_name()
    {
        return this.getEntityName();
    }

    @Override
    public @NotNull BaseComponent[] get_display_name()
    {
        return ConversionUtils.to_elytra_text(this.getDisplayName());
    }

    @Override
    public boolean has_custom_name()
    {
        return this.hasCustomName();
    }

    @Override
    public @NotNull Optional<BaseComponent[]> get_custom_name()
    {
        return Optional.ofNullable(this.getCustomName()).map(ConversionUtils::to_elytra_text);
    }

    @Override
    public void set_custom_name(BaseComponent... custom_name)
    {
        this.setCustomName(ConversionUtils.to_minecraft_text(custom_name));
    }

    @Override
    public boolean is_custom_name_visible()
    {
        return this.isCustomNameVisible();
    }

    @Override
    public void set_custom_name_visible(boolean custom_name_visible)
    {
        this.setCustomNameVisible(custom_name_visible);
    }

    @Override
    public int get_max_fire_ticks()
    {
        return 0;
    }

    @Override
    public int get_fire_ticks()
    {
        return this.fireTicks;
    }

    @Override
    public void set_fire_ticks(int ticks)
    {
        this.fireTicks = ticks;
    }

    @Override
    public boolean is_on_fire()
    {
        return this.isOnFire();
    }

    @Override
    public boolean is_fire_immune()
    {
        return this.isFireImmune();
    }

    @Override
    public boolean is_on_ground()
    {
        return this.onGround;
    }

    @Override
    public boolean is_touching_water()
    {
        return this.isInsideWater();
    }

    @Override
    public boolean is_submerged_in_water()
    {
        return this.isInWater();
    }

    @Override
    public boolean is_being_rain_on()
    {
        return this.isBeingRainedOn();
    }

    @Override
    public boolean is_inside_water_bubble_column()
    {
        return this.isInsideBubbleColumn();
    }

    @Override
    public boolean is_silent()
    {
        return this.isSilent();
    }

    @Override
    public void set_silent(boolean silent)
    {
        this.setSilent(silent);
    }

    @Override
    public boolean has_gravity()
    {
        return !this.hasNoGravity();
    }

    @Override
    public void set_gravity(boolean gravity)
    {
        this.setNoGravity(!gravity);
    }

    @Override
    public boolean is_glowing()
    {
        return this.glowing;
    }

    @Override
    public void set_glowing(boolean glowing)
    {
        this.glowing = glowing;
    }

    @Override
    public boolean is_invisible()
    {
        return this.isInvisible();
    }

    @Override
    public void set_invisible(boolean invisible)
    {
        this.setInvisible(invisible);
    }

    @Override
    public boolean is_invulnerable()
    {
        return this.invulnerable;
    }

    @Override
    public void set_invulnerable(boolean invulnerable)
    {
        this.invulnerable = invulnerable;
    }

    @Override
    public @NotNull Server get_server()
    {
        // We're in a server environment so it should not be null.
        return ((ServerGet) this.getServer()).get_server();
    }
}

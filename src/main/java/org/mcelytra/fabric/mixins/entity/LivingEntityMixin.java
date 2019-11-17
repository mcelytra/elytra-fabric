/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of mcelytra.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.mcelytra.fabric.mixins.entity;

import net.minecraft.advancement.criterion.Criterions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import org.mcelytra.core.Arm;
import org.mcelytra.core.Hand;
import org.mcelytra.core.entity.EntityLiving;
import org.mcelytra.core.entity.EntityPlayer;
import org.mcelytra.core.event.entity.EntityDeathEvent;
import org.mcelytra.core.event.entity.EntityResurrectEvent;
import org.mcelytra.fabric.utils.ConversionUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin implements EntityLiving
{
    @Shadow
    public abstract float getHealth();

    @Shadow
    public abstract void setHealth(float float_1);

    @Shadow
    public abstract float getMaximumHealth();

    @Shadow
    private float absorptionAmount;

    @Shadow
    public abstract void setAbsorptionAmount(float float_1);

    @Shadow
    public abstract int getStuckArrowCount();

    @Shadow
    public abstract void setStuckArrowCount(int int_1);

    @Shadow
    public abstract int getStingerCount();

    @Shadow
    public abstract void setStingerCount(int int_1);

    @Shadow
    public abstract float getMovementSpeed();

    @Shadow
    public abstract void setMovementSpeed(float float_1);

    @Shadow
    public abstract boolean isBaby();

    @Shadow
    public abstract boolean isSleeping();

    @Shadow
    public abstract boolean isUsingItem();

    @Shadow
    public abstract ItemStack getStackInHand(net.minecraft.util.Hand hand_1);

    @Shadow public abstract boolean clearStatusEffects();

    @Shadow public abstract boolean addStatusEffect(StatusEffectInstance statusEffectInstance_1);



    @Override
    public boolean is_baby()
    {
        return this.isBaby();
    }

    @Override
    public float get_health()
    {
        return this.getHealth();
    }

    @Override
    public void set_health(float health)
    {
        this.setHealth(health);
    }

    @Override
    public float get_maximum_health()
    {
        return this.getMaximumHealth();
    }

    @Override
    public void set_maximum_health(float maximum_health)
    {

    }

    @Override
    public float get_absorption_amount()
    {
        return this.absorptionAmount;
    }

    @Override
    public void set_absorption_amount(float absorption_amount)
    {
        this.setAbsorptionAmount(absorption_amount);
    }

    @Override
    public int get_stuck_arrow_count()
    {
        return this.getStuckArrowCount();
    }

    @Override
    public void set_stuck_arrow_count(int arrows)
    {
        this.setStuckArrowCount(arrows);
    }

    @Override
    public int get_stinger_count()
    {
        return this.getStingerCount();
    }

    @Override
    public void set_stinger_count(int stingers)
    {
        this.setStingerCount(stingers);
    }

    @Override
    public float get_movement_speed()
    {
        return this.getMovementSpeed();
    }

    @Override
    public void set_movement_speed(float movement_speed)
    {
        this.setMovementSpeed(movement_speed);
    }

    @Override
    public Arm get_main_arm()
    {
        return null;
    }

    @Override
    public boolean is_using_item()
    {
        return this.isUsingItem();
    }

    @Override
    public Hand get_active_hand()
    {
        return null;
    }

    @Override
    public boolean is_sleeping()
    {
        return this.isSleeping();
    }

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void on_death(DamageSource damage_source, CallbackInfo ci)
    {
        EntityDeathEvent event = new EntityDeathEvent(this);
        this.get_server().get_addon_manager().fire_event(event);
    }

    /**
     * @author LambdAurora
     * @reason Elytra event injection.
     */
    @Overwrite
    private boolean tryUseTotem(DamageSource damage_source)
    {
        if (damage_source.isOutOfWorld()) {
            return false;
        } else {
            // ELYTRA BEGIN
            ItemStack mc_original_stack = null;
            Hand hand = null;

            for (Hand h : Hand.values()) {
                ItemStack stack = this.getStackInHand(ConversionUtils.to_minecraft_hand(h));
                if (stack.getItem() == Items.TOTEM_OF_UNDYING) {
                    mc_original_stack = stack;
                    hand = h;
                    break;
                }
            }

            if (mc_original_stack == null)
                return false;

            ItemStack mc_stack = mc_original_stack.copy();

            EntityResurrectEvent event = new EntityResurrectEvent(this, hand);
            this.get_server().get_addon_manager().fire_event(event);
            if (event.is_cancelled())
                return false;

            if (event.does_consume_item())
                mc_original_stack.decrement(1);

            if (this instanceof EntityPlayer) {
                ServerPlayerEntity serverPlayerEntity_1 = (ServerPlayerEntity) (EntityPlayer) this;
                serverPlayerEntity_1.incrementStat(Stats.USED.getOrCreateStat(Items.TOTEM_OF_UNDYING));
                Criterions.USED_TOTEM.trigger(serverPlayerEntity_1, mc_stack);
            }

            this.set_health(1.0F);
            this.clearStatusEffects();
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 900, 1));
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1));
            this.world.sendEntityStatus((net.minecraft.entity.Entity) (Object) this, (byte) 35);

            return true;
        }
    }
}

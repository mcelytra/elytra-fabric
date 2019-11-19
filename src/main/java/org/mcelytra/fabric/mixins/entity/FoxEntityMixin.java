package org.mcelytra.fabric.mixins.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.world.World;
import org.mcelytra.core.FoxType;
import org.mcelytra.core.entity.passive.EntityFox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FoxEntity.class)
public abstract class FoxEntityMixin extends AnimalEntity implements EntityFox
{
    @Shadow public abstract boolean isSitting();

    @Shadow public abstract void setSitting(boolean sitting);

    @Shadow protected abstract boolean isAggressive();

    @Shadow protected abstract void setAggressive(boolean aggressive);

    @Shadow protected abstract void setSleeping(boolean sleeping);

    protected FoxEntityMixin(EntityType<? extends AnimalEntity> type, World world)
    {
        super(type, world);
    }

    @Override
    public FoxType get_type()
    {
        return null;
    }

    @Override
    public void set_type(FoxType type)
    {

    }

    @Override
    public boolean is_sitting()
    {
        return this.isSitting();
    }

    @Override
    public void set_sitting(boolean sitting)
    {
        this.setSitting(sitting);
    }

    @Override
    public boolean is_aggressive()
    {
        return this.isAggressive();
    }

    @Override
    public void set_aggressive(boolean aggressive)
    {
        this.setAggressive(aggressive);
    }

    @Override
    public void set_sleeping(boolean sleeping)
    {
        this.setSleeping(sleeping);
    }
}

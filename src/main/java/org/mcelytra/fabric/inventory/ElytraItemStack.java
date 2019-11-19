/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of mcelytra.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.mcelytra.fabric.inventory;

import org.jetbrains.annotations.NotNull;
import org.mcelytra.core.inventory.ItemStack;
import org.mcelytra.core.item.Item;
import org.mcelytra.core.item.ItemConvertible;

public class ElytraItemStack extends ItemStack
{
    private net.minecraft.item.ItemStack handle;

    public ElytraItemStack(ItemConvertible item)
    {
        this(item, 1);
    }

    public ElytraItemStack(ItemConvertible item, int count)
    {
        super(item, count);
    }

    public ElytraItemStack(@NotNull net.minecraft.item.ItemStack stack)
    {
        super((ItemConvertible) stack.getItem(), 0);
        this.handle = stack;
    }

    public net.minecraft.item.ItemStack get_handle()
    {
        return this.handle;
    }

    @Override
    public Item get_item()
    {
        return (Item) this.handle.getItem();
    }

    @Override
    public int get_count()
    {
        return this.handle.getCount();
    }

    @Override
    public void set_count(int count)
    {
        this.handle.setCount(count);
    }

    @Override
    public boolean is_empty()
    {
        return this.handle.isEmpty();
    }

    @Override
    public ItemStack copy()
    {
        return new ElytraItemStack(this.handle.copy());
    }

    /**
     * Returns a new item stack from a copied minecraft item stack.
     * @param stack The minecraft's item stack.
     * @return The copied Elytra item stack.
     */
    public static ElytraItemStack from_copy(net.minecraft.item.ItemStack stack)
    {
        return new ElytraItemStack(stack.copy());
    }

    /**
     * Represents the implementation.
     */
    public interface Implementation
    {
        ItemStack as_eltyra();
    }
}

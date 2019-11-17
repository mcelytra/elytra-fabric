package org.mcelytra.fabric.gen.config;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.decorator.TreeDecorator;
import net.minecraft.world.gen.decorator.TreeDecoratorType;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.stateprovider.StateProvider;
import net.minecraft.world.gen.stateprovider.StateProviderType;

import java.util.List;

public class FallenTreeFeatureConfig extends TreeFeatureConfig
{
    public final int     variance;
    public final boolean snowy;
    public final boolean mushrooms;

    protected FallenTreeFeatureConfig(StateProvider trunk_provider, StateProvider leaves_provider, List<TreeDecorator> decorators, int base_height, int variance, boolean snowy, boolean mushrooms)
    {
        super(trunk_provider, leaves_provider, decorators, base_height);
        this.variance = variance;
        this.snowy = snowy;
        this.mushrooms = mushrooms;
    }

    @Override
    public <T> Dynamic<T> serialize(DynamicOps<T> ops)
    {
        com.google.common.collect.ImmutableMap.Builder<T, T> builder = ImmutableMap.builder();
        builder.put(ops.createString("trunk_provider"), this.trunkProvider.serialize(ops))
                .put(ops.createString("leaves_provider"), this.leavesProvider.serialize(ops))
                .put(ops.createString("decorators"), ops.createList(this.decorators.stream().map((treeDecorator) -> treeDecorator.serialize(ops))))
                .put(ops.createString("base_height"), ops.createInt(this.baseHeight))
                .put(ops.createString("variance"), ops.createInt(this.variance))
                .put(ops.createString("snowy"), ops.createBoolean(this.snowy))
                .put(ops.createString("mushrooms"), ops.createBoolean(this.mushrooms));
        return new Dynamic<>(ops, ops.createMap(builder.build()));
    }

    @SuppressWarnings("unchecked")
    public static <T> FallenTreeFeatureConfig deserialize(Dynamic<T> config_deserializer)
    {
        StateProviderType<?> trunk_provider = Registry.BLOCK_STATE_PROVIDER_TYPE.get(new Identifier(config_deserializer.get("trunk_provider").get("type").asString().orElseThrow(RuntimeException::new)));
        StateProviderType<?> leaves_provider = Registry.BLOCK_STATE_PROVIDER_TYPE.get(new Identifier(config_deserializer.get("leaves_provider").get("type").asString().orElseThrow(RuntimeException::new)));
        return new FallenTreeFeatureConfig(trunk_provider.deserialize(config_deserializer.get("trunk_provider").orElseEmptyMap()),
                leaves_provider.deserialize(config_deserializer.get("leaves_provider").orElseEmptyMap()),
                config_deserializer.get("decorators").asList((dynamic) -> ((TreeDecoratorType) Registry.TREE_DECORATOR_TYPE.get(new Identifier(dynamic.get("type").asString().orElseThrow(RuntimeException::new)))).method_23472(dynamic)),
                config_deserializer.get("base_height").asInt(3),
                config_deserializer.get("variance").asInt(2),
                config_deserializer.get("snowy").asBoolean(false),
                config_deserializer.get("mushrooms").asBoolean(true));
    }

    public static class Builder
    {
        public final StateProvider       trunk_provider;
        public final StateProvider       leaves_provider;
        private      List<TreeDecorator> decorators  = Lists.newArrayList();
        private      int                 base_height = 3;
        private      int                 variance    = 2;
        private      boolean             snowy       = false;
        private      boolean             mushrooms   = true;


        public Builder(StateProvider trunk_provider, StateProvider leaves_provider)
        {
            this.trunk_provider = trunk_provider;
            this.leaves_provider = leaves_provider;
        }

        public FallenTreeFeatureConfig.Builder base_height(int base_height)
        {
            this.base_height = base_height;
            return this;
        }

        public FallenTreeFeatureConfig.Builder variance(int variance)
        {
            this.variance = variance;
            return this;
        }

        public FallenTreeFeatureConfig.Builder snowy(boolean snowy)
        {
            this.snowy = snowy;
            if (snowy)
                this.mushrooms = false;
            return this;
        }

        public FallenTreeFeatureConfig.Builder mushrooms(boolean mushrooms)
        {
            this.mushrooms = mushrooms;
            return this;
        }

        public FallenTreeFeatureConfig build()
        {
            return new FallenTreeFeatureConfig(this.trunk_provider, this.leaves_provider, this.decorators, this.base_height, this.variance, this.snowy, this.mushrooms);
        }
    }
}

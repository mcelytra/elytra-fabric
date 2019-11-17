/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of mcelytra.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.mcelytra.fabric.gen;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LogBlock;
import net.minecraft.block.VineBlock;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.IWorld;
import net.minecraft.world.ModifiableTestableWorld;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import org.aperlambda.lambdacommon.utils.Pair;
import org.mcelytra.fabric.gen.config.FallenTreeFeatureConfig;

import java.util.*;
import java.util.function.Function;

public class FallenTreeFeature extends AbstractTreeFeature<FallenTreeFeatureConfig>
{
    public FallenTreeFeature(Function<Dynamic<?>, ? extends FallenTreeFeatureConfig> config_factory)
    {
        super(config_factory);
    }

    @Override
    protected boolean generate(ModifiableTestableWorld modifiableTestableWorld, Random random, BlockPos blockPos, Set<BlockPos> set, Set<BlockPos> set2, BlockBox blockBox, FallenTreeFeatureConfig treeFeatureConfig)
    {
        return false;
    }

    @Override
    public boolean generate(IWorld world, ChunkGenerator<? extends ChunkGeneratorConfig> generator, Random random, BlockPos origin, FallenTreeFeatureConfig config)
    {
        // The fallen tree length.
        int length = config.baseHeight + random.nextInt(config.variance);

        Direction.Axis axis = random.nextBoolean() ? Direction.Axis.X : Direction.Axis.Z;
        Direction direction = Direction.from(axis, random.nextBoolean() ? Direction.AxisDirection.POSITIVE : Direction.AxisDirection.NEGATIVE);

        BlockPos below = origin.down(1);
        if (!isNaturalDirtOrGrass(world, below) || !is_air_or_vegetation(world, origin))
            return false;

        boolean result;
        int i = 0;
        while (!(result = generate(world, random, origin, length, direction, config)) && i < 2) {
            direction = direction.rotateYClockwise();
            i++;
        }

        return result;
    }

    private boolean generate(IWorld world, Random random, BlockPos origin, int length, Direction direction, FallenTreeFeatureConfig config)
    {
        Direction.Axis axis = direction.getAxis();

        BlockPos.Mutable pos = new BlockPos.Mutable(origin);

        List<BlockPos> blocks = new ArrayList<>();

        int air = 0;
        pos.setOffset(direction);
        Direction log_direction = direction;
        BlockPos last = pos;
        for (int n = 0; n < length; n++) {
            Optional<Pair<BlockPos, Pair<Boolean, Direction>>> res = can_generate(world, last, direction, log_direction);

            if (!res.isPresent())
                return false;

            if (res.get().get_value().get_key())
                air++;
            log_direction = res.get().get_value().get_value();

            blocks.add((last = res.get().get_key()));
        }

        // No floating logs.
        if (air * 2 > length) {
            return false;
        }

        pos.set(origin);

        if (blocks.size() != length) {
            return false;
        }

        // First log
        setBlockState(world, pos, config.trunkProvider.getBlockState(random, pos).with(LogBlock.AXIS, Direction.Axis.Y));
        if (!config.snowy) {
            Direction tmp = direction;
            for (int i = 0; i < 3; i++) {
                tmp = tmp.rotateYClockwise();
                if (random.nextBoolean() && is_air_or_vegetation(world, origin.offset(tmp)))
                    setBlockState(world, origin.offset(tmp), Blocks.VINE.getDefaultState().with(VineBlock.getFacingProperty(tmp.getOpposite()), true));
            }
        }

        blocks.stream().peek(place_to -> setBlockState(world, place_to, config.trunkProvider.getBlockState(random, pos).with(LogBlock.AXIS, axis)))
                .map(place_to -> place_to.down(1))
                .filter(place_to -> isNaturalDirtOrGrass(world, place_to))
                .forEach(place_to -> setBlockState(world, place_to, Blocks.DIRT.getDefaultState()));

        if (!config.snowy) {
            blocks.stream().filter(place_to -> random.nextBoolean())
                    .map(place_to -> {
                        Direction vine_direction = random.nextBoolean() ? direction.rotateYClockwise() : direction.rotateYCounterclockwise();
                        return Pair.of(place_to.offset(vine_direction), vine_direction);
                    })
                    .filter(data -> is_air_or_vegetation(world, data.get_key()))
                    .forEach(data -> setBlockState(world, data.get_key(), Blocks.VINE.getDefaultState().with(VineBlock.getFacingProperty(data.get_value().getOpposite()), true)));
            if (config.mushrooms)
                blocks.stream().filter(place_to -> random.nextInt(3) == 0)
                        .map(place_to -> place_to.offset(Direction.UP))
                        .filter(place_to -> is_air_or_vegetation(world, place_to))
                        .forEach(place_to -> world.setBlockState(place_to, Blocks.BROWN_MUSHROOM.getDefaultState(), 2));
        }
        return true;
    }

    private boolean is_air_or_vegetation(TestableWorld world, BlockPos pos)
    {
        return world.testBlockState(pos, (state) -> state.isAir() || state.matches(BlockTags.LEAVES) || state.matches(BlockTags.FLOWERS) || state.getBlock() == Blocks.GRASS || state.getBlock() == Blocks.FERN || state.getBlock() == Blocks.SNOW);
    }

    /**
     * Checks if we can generate the fallen tree at the next position following the direction.
     *
     * @param world         The world.
     * @param last_pos      The last position of the log block.
     * @param direction     The direction where the fallen tree goes.
     * @param log_direction The direction to define if the tree logs go up or down. At first run, the value is undefined and same as direction.
     * @return An optional value of the log position, if the block below is air and the log direction (may be non-equal to log_direction if log_direction was equals to direction).
     */
    private Optional<Pair<BlockPos, Pair<Boolean, Direction>>> can_generate(TestableWorld world, BlockPos last_pos, Direction direction, Direction log_direction)
    {
        BlockPos.Mutable pos = new BlockPos.Mutable(last_pos.offset(direction));

        // Check if the log block can be generated and is not conflicting with another block.
        if (!is_air_or_vegetation(world, pos)) {
            if (log_direction != Direction.UP) {
                if (log_direction == direction)
                    log_direction = Direction.UP;
                else
                    return Optional.empty();
            }

            if (!is_air_or_vegetation(world, pos.setOffset(log_direction)))
                return Optional.empty();
        }

        // If the block under the log block is air.
        boolean air = false;

        // Check for air blocks (and go down if possible)
        if (!world.testBlockState(pos.setOffset(Direction.DOWN), BlockState::isOpaque)) {
            // We can't go down so count the air block.
            if (log_direction != Direction.DOWN && log_direction != direction)
                air = true;
                // The log direction is undefined, as we go down, set it to down.
            else if (log_direction == direction) {
                log_direction = Direction.DOWN;
                // The block down is still air, so count the air.
                if (!world.testBlockState(pos.setOffset(log_direction), BlockState::isOpaque))
                    air = true;
                // Log direction is already defined, same as before: if the block down is still air then count the air.
            } else if (!world.testBlockState(pos.setOffset(log_direction), BlockState::isOpaque))
                air = true;
        }

        // Goes up again for the log block as the old value is for the block below.
        pos.setOffset(Direction.UP);
        return Optional.of(Pair.of(pos.toImmutable(), Pair.of(air, log_direction)));
    }

    private boolean can_generate_lower(int i, int length, boolean previous)
    {
        if (previous)
            return true;
        if (i >= length / 2)
            return true;
        return i == 0;
    }
}

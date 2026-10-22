package juuxel.woodsandmires.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import juuxel.woodsandmires.block.ShrubLogBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;

public record ShrubFeature(BlockState log, BlockState leaves, int baseHeight, int extraHeight, float extraHeightChance) implements Feature {
    public static final MapCodec<ShrubFeature> CODEC = RecordCodecBuilder.mapCodec(
        instance -> instance.group(
            BlockState.CODEC.fieldOf("log").forGetter(ShrubFeature::log),
            BlockState.CODEC.fieldOf("leaves").forGetter(ShrubFeature::leaves),
            Codec.INT.fieldOf("base_height").forGetter(ShrubFeature::baseHeight),
            Codec.INT.fieldOf("extra_height").forGetter(ShrubFeature::extraHeight),
            Codec.FLOAT.fieldOf("extra_height_chance").forGetter(ShrubFeature::extraHeightChance)
        ).apply(instance, ShrubFeature::new)
    );

    @Override
    public MapCodec<ShrubFeature> codec() {
        return CODEC;
    }

    @Override
    public boolean place(WorldGenLevel world, ChunkGenerator chunkGenerator, RandomSource random, BlockPos pos) {
        BlockPos below = pos.below();
        if (!world.isStateAtPosition(below, blockState -> blockState.is(BlockTags.DIRT)) || !world.getBlockState(below).isFaceSturdy(world, below, Direction.UP)) {
            return false;
        }

        BlockPos.MutableBlockPos mut = new BlockPos.MutableBlockPos();
        mut.set(pos);

        BlockState log = this.log.trySetValue(RotatedPillarBlock.AXIS, Direction.Axis.Y);
        BlockState logWithLeaves = log.getBlock() instanceof ShrubLogBlock ? log.setValue(ShrubLogBlock.HAS_LEAVES, true) : log;
        BlockState leaves = this.leaves.setValue(LeavesBlock.DISTANCE, 1);
        int extraHeight = random.nextFloat() < extraHeightChance ? random.nextInt(this.extraHeight + 1) : 0;
        int height = baseHeight + extraHeight;

        for (int y = 1; y <= height; y++) {
            if (y > 1 || height == 1) {
                setBlock(world, mut, logWithLeaves);

                for (Direction direction : Direction.Plane.HORIZONTAL) {
                    mut.move(direction);
                    setBlock(world, mut, leaves);
                    mut.move(direction.getOpposite());
                }
            } else {
                setBlock(world, mut, log);
            }

            mut.move(Direction.UP);
        }

        setBlock(world, mut, leaves);
        return false;
    }
}

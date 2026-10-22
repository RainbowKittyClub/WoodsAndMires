package juuxel.woodsandmires.feature;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import juuxel.woodsandmires.block.AgedLogBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.FloatProviders;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviders;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import java.util.ArrayList;
import java.util.List;

public record FallenLogFeature(Block mainLog, Block agedLog, FloatProvider agedHeightFraction,
                               IntProvider length, Holder<BlockStateProvider> topDecoration) implements Feature {
    public static final MapCodec<FallenLogFeature> CODEC = RecordCodecBuilder.mapCodec(
        instance -> instance.group(
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("main_log").forGetter(FallenLogFeature::mainLog),
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("aged_log").forGetter(FallenLogFeature::agedLog),
            FloatProviders.CODEC.fieldOf("aged_height_fraction").forGetter(FallenLogFeature::agedHeightFraction),
            IntProviders.POSITIVE_CODEC.fieldOf("length").forGetter(FallenLogFeature::length),
            BlockStateProvider.CODEC.fieldOf("top_decoration").forGetter(FallenLogFeature::topDecoration)
        ).apply(instance, FallenLogFeature::new)
    );

    public FallenLogFeature(Block mainLog, Block agedLog, FloatProvider agedHeightFraction, IntProvider length, BlockStateProvider topDecoration) {
        this(mainLog, agedLog, agedHeightFraction, length, Holder.direct(topDecoration));
    }

    @Override
    public MapCodec<FallenLogFeature> codec() {
        return CODEC;
    }

    @Override
    public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin) {
        BlockPos.MutableBlockPos mut = origin.mutable();
        if (!canPlace(level, mut)) {
            return false;
        }

        Direction.Axis axis = Direction.Plane.HORIZONTAL.getRandomAxis(random);
        // We need to correct it so that the "mid" texture will align correctly.
        Direction direction = axis == Direction.Axis.X ? Direction.WEST : Direction.SOUTH;
        int length = this.length.sample(random);
        int mid = (int) (agedHeightFraction.sample(random) * length);
        List<BlockPos.MutableBlockPos> trunkPositions = new ArrayList<>();

        if (random.nextInt(5) == 0) {
            for (int i = 0; i < length; i++) {
                if (!canPlace(level, mut)) {
                    break;
                }

                Block block = i < mid ? mainLog : agedLog;
                BlockState state = block.defaultBlockState().setValue(RotatedPillarBlock.AXIS, axis);

                if (i == mid) {
                    state = state.setValue(AgedLogBlock.MID, true);
                }

                setBlock(level, mut, state);
                trunkPositions.add(mut.mutable());
                mut.move(direction);
            }
        } else {
            Block block = random.nextBoolean() ? mainLog : agedLog;
            BlockState state = block.defaultBlockState().setValue(RotatedPillarBlock.AXIS, axis);

            for (int i = 0; i < length; i++) {
                if (!canPlace(level, mut)) {
                    break;
                }

                setBlock(level, mut, state);
                trunkPositions.add(mut.mutable());
                mut.move(direction);
            }
        }

        for (BlockPos.MutableBlockPos pos : trunkPositions) {
            pos.move(Direction.UP);
            BlockState state = topDecoration.value().getState(level, random, pos);
            if (!state.isAir() && state.canSurvive(level, pos)) {
                setBlock(level, pos, state);
            }
        }

        return true;
    }

    private static boolean canPlace(LevelSimulatedReader world, BlockPos.MutableBlockPos mut) {
        if (!world.isStateAtPosition(mut, BlockState::isAir)) return false;
        mut.move(0, -1, 0);
        boolean result = world.isStateAtPosition(mut, blockState -> blockState.is(BlockTags.SUBSTRATE_OVERWORLD));
        mut.move(0, 1, 0);
        return result;
    }
}

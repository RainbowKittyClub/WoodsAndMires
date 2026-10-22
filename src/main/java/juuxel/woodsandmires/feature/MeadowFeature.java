package juuxel.woodsandmires.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record MeadowFeature(Holder<BlockStateProvider> stateProvider, float chance, BlockPredicate allowedPlacement) implements Feature {
    public static final MapCodec<MeadowFeature> CODEC = RecordCodecBuilder.mapCodec(
        instance -> instance.group(
            BlockStateProvider.CODEC.fieldOf("state_provider").forGetter(MeadowFeature::stateProvider),
            Codec.FLOAT.fieldOf("chance").forGetter(MeadowFeature::chance),
            BlockPredicate.CODEC.optionalFieldOf("allowed_placement", BlockPredicate.alwaysTrue()).forGetter(MeadowFeature::allowedPlacement)
        ).apply(instance, MeadowFeature::new)
    );

    public MeadowFeature(BlockStateProvider stateProvider, float chance) {
        this(Holder.direct(stateProvider), chance, BlockPredicate.alwaysTrue());
    }

    @Override
    public MapCodec<MeadowFeature> codec() {
        return CODEC;
    }

    @Override
    public boolean place(WorldGenLevel world, ChunkGenerator chunkGenerator, RandomSource random, BlockPos pos) {
        BlockPos.MutableBlockPos mut = new BlockPos.MutableBlockPos();
        boolean generated = false;

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                if (random.nextFloat() > chance) continue;

                int xo = pos.getX() + x;
                int zo = pos.getZ() + z;
                int y = world.getHeight(Heightmap.Types.MOTION_BLOCKING, xo, zo);
                mut.set(xo, y, zo);

                if (!allowedPlacement.test(world, mut)) continue;

                BlockState vegetation = stateProvider.value().getState(world, random, mut);
                if (world.isEmptyBlock(mut) && vegetation.canSurvive(world, mut)) {
                    setBlock(world, mut, vegetation);
                    generated = true;
                }
            }
        }

        return generated;
    }
}

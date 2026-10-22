package juuxel.woodsandmires.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviders;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public record FellPondFeature(IntProvider radius, IntProvider depth, Holder<BlockStateProvider> fillBlock,
                              Holder<BlockStateProvider> border,
                              Holder<BlockStateProvider> bottomBlock, float bottomReplaceChance) implements Feature {
    public static final MapCodec<FellPondFeature> CODEC = RecordCodecBuilder.mapCodec(
        instance -> instance.group(
            IntProviders.POSITIVE_CODEC.fieldOf("radius").forGetter(FellPondFeature::radius),
            IntProviders.POSITIVE_CODEC.fieldOf("depth").forGetter(FellPondFeature::depth),
            BlockStateProvider.CODEC.fieldOf("fill_block").forGetter(FellPondFeature::fillBlock),
            BlockStateProvider.CODEC.fieldOf("border").forGetter(FellPondFeature::border),
            BlockStateProvider.CODEC.fieldOf("bottom_block").forGetter(FellPondFeature::bottomBlock),
            Codec.FLOAT.fieldOf("bottom_replace_chance").forGetter(FellPondFeature::bottomReplaceChance)
        ).apply(instance, FellPondFeature::new)
    );
    private static final List<Direction> BORDER_DIRECTIONS = List.of(Direction.DOWN, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);
    private static final int MAX_HEIGHT_TO_DIG = 2;

    @Override
    public MapCodec<FellPondFeature> codec() {
        return CODEC;
    }

    @Override
    public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos originPos) {
        BlockPos.MutableBlockPos origin = originPos.mutable().move(0, -1, 0);

        // Don't generate in water.
        if (!level.getFluidState(origin).isEmpty()) {
            return false;
        }

        int depth = this.depth.sample(random);
        int semiMajor = radius.sample(random);
        int semiMinor = radius.sample(random);
        BlockPos.MutableBlockPos mut = new BlockPos.MutableBlockPos();
        Set<BlockPos> filledPositions = new HashSet<>();
        float theta = random.nextFloat() * Mth.TWO_PI;

        int dugHeight = 0;
        // Check for air layers
        boolean foundAir;
        do {
            foundAir = false;
            float semiMajorSq = semiMajor * semiMajor;
            float semiMinorSq = semiMinor * semiMinor;

            outer: for (int x = -semiMajor; x <= semiMajor; x++) {
                for (int z = -semiMinor; z <= semiMinor; z++) {
                    if (isInsideEllipse(x, z, semiMajorSq, semiMinorSq, theta) && level.isEmptyBlock(mut.set(origin).move(x, 0, z))) {
                        foundAir = true;
                        break outer;
                    }
                }
            }

            if (foundAir) {
                for (int x = -semiMajor; x <= semiMajor; x++) {
                    for (int z = -semiMinor; z <= semiMinor; z++) {
                        if (isInsideEllipse(x, z, semiMajorSq, semiMinorSq, theta)) {
                            setBlock(level, mut.set(origin).move(x, 0, z), Blocks.AIR.defaultBlockState());
                        }
                    }
                }

                origin.move(0, -1, 0);
                dugHeight++;

                if (dugHeight >= MAX_HEIGHT_TO_DIG) {
                    break;
                }
            }
        } while (foundAir);

        for (int yo = 0; yo < depth; yo++) {
            filledPositions.clear();
            float semiMajorSq = semiMajor * semiMajor;
            float semiMinorSq = semiMinor * semiMinor;

            for (int x = -semiMajor; x <= semiMajor; x++) {
                for (int z = -semiMinor; z <= semiMinor; z++) {
                    if (isInsideEllipse(x, z, semiMajorSq, semiMinorSq, theta)) {
                        mut.set(origin.getX() + x, origin.getY() - yo, origin.getZ() + z);
                        setBlock(level, mut, fillBlock.value().getState(level, random, mut));
                        filledPositions.add(mut.immutable());

                        for (Direction d : BORDER_DIRECTIONS) {
                            mut.move(d);

                            if (!filledPositions.contains(mut) && shouldPlaceBorder(level, mut)) {
                                setBlock(level, mut, border.value().getState(level, random, mut));
                            }

                            mut.move(d.getOpposite());
                        }

                        if (random.nextFloat() < bottomReplaceChance) {
                            mut.move(0, -1, 0);
                            setBlock(level, mut, bottomBlock.value().getState(level, random, mut));
                        }
                    }
                }
            }

            semiMajor--;
            semiMinor--;
        }

        return true;
    }

    public static final class Builder {
        private IntProvider radius;
        private IntProvider depth;
        private BlockStateProvider fillBlock;
        private BlockStateProvider border;
        private BlockStateProvider bottomBlock;
        private Float bottomReplaceChance;

        public Builder radius(IntProvider radius) {
            this.radius = radius;
            return this;
        }

        public Builder depth(IntProvider depth) {
            this.depth = depth;
            return this;
        }

        public Builder fillWith(BlockStateProvider fillBlock) {
            this.fillBlock = fillBlock;
            return this;
        }

        public Builder border(BlockStateProvider border) {
            this.border = border;
            return this;
        }

        public Builder bottomBlock(BlockStateProvider bottomBlock, float bottomReplaceChance) {
            this.bottomBlock = bottomBlock;
            this.bottomReplaceChance = bottomReplaceChance;
            return this;
        }

        public FellPondFeature build() {
            return new FellPondFeature(
                Objects.requireNonNull(radius, "radius"),
                Objects.requireNonNull(depth, "depth"),
                Holder.direct(Objects.requireNonNull(fillBlock, "fillBlock")),
                Holder.direct(Objects.requireNonNull(border, "border")),
                Holder.direct(Objects.requireNonNull(bottomBlock, "bottomBlock")),
                Objects.requireNonNull(bottomReplaceChance, "bottomReplaceChance")
            );
        }
    }

    static boolean isInsideEllipse(int x, int y, float semiMajorSq, float semiMinorSq, float theta) {
        float sin = Mth.sin(theta);
        float cos = Mth.cos(theta);
        float sinSq = sin * sin;
        float cosSq = cos * cos;

        // https://en.wikipedia.org/wiki/Ellipse#General_ellipse
        // Ax^2 + Bxy + Cy^2 + Dx + Ey + F = 0
        // Since we're centred at the origin, D and E are 0.
        float a = semiMajorSq * sinSq + semiMinorSq * cosSq;
        float b = 2 * (semiMinorSq - semiMajorSq) * sin * cos;
        float c = semiMajorSq * cosSq + semiMinorSq * sinSq;
        float f = -semiMajorSq * semiMinorSq;

        return (a * x * x) + (b * x * y) + (c * y * y) + f <= 0;
    }

    private static boolean shouldPlaceBorder(WorldGenLevel world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return state.isAir() || !state.isCollisionShapeFullBlock(world, pos);
    }
}

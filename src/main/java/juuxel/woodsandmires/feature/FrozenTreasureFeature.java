package juuxel.woodsandmires.feature;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviders;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record FrozenTreasureFeature(
    Holder<BlockStateProvider> ice,
    IntProvider height,
    IntProvider radius,
    ResourceKey<LootTable> lootTable
) implements Feature {
    public static final MapCodec<FrozenTreasureFeature> CODEC = RecordCodecBuilder.mapCodec(builder ->
        builder.group(
            BlockStateProvider.CODEC.fieldOf("ice").forGetter(FrozenTreasureFeature::ice),
            IntProviders.POSITIVE_CODEC.fieldOf("height").forGetter(FrozenTreasureFeature::height),
            IntProviders.POSITIVE_CODEC.fieldOf("radius").forGetter(FrozenTreasureFeature::radius),
            ResourceKey.codec(Registries.LOOT_TABLE).fieldOf("loot_table").forGetter(FrozenTreasureFeature::lootTable)
        ).apply(builder, FrozenTreasureFeature::new)
    );

    public FrozenTreasureFeature(BlockStateProvider ice, IntProvider height, IntProvider radius, ResourceKey<LootTable> lootTable) {
        this(Holder.direct(ice), height, radius, lootTable);
    }

    @Override
    public MapCodec<FrozenTreasureFeature> codec() {
        return CODEC;
    }

    @Override
    public boolean place(WorldGenLevel world, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin) {
        int height = this.height.sample(random);
        int semiMajor = radius.sample(random);
        int semiMinor = radius.sample(random);
        BlockPos.MutableBlockPos mut = new BlockPos.MutableBlockPos();
        int chestY = height / 3;

        for (int x = -semiMajor; x <= semiMajor; x++) {
            for (int z = -semiMinor; z <= semiMinor; z++) {
                mut.set(origin.getX() + x, origin.getY() - 1, origin.getZ() + z);
                if (!hasSolidTop(world, mut) || !world.isFluidAtPosition(mut, FluidState::isEmpty)) {
                    return false;
                }
            }
        }

        for (int yo = -1; yo < height; yo++) {
            float width = getWidthAdjustmentFromProgress(Mth.inverseLerp(yo, -1f, height - 1f));
            int sma = Math.round(semiMajor * width);
            int smi = Math.round(semiMinor * width);
            float semiMajorSq = sma * sma;
            float semiMinorSq = smi * smi;

            for (int x = -sma; x <= sma; x++) {
                for (int z = -smi; z <= smi; z++) {
                    mut.set(origin.getX() + x, origin.getY() + yo, origin.getZ() + z);
                    if (x == 0 && z == 0 && yo == chestY) {
                        BlockState chest = Blocks.CHEST.defaultBlockState()
                            .setValue(ChestBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection(random));
                        setBlock(world, mut, chest);
                        RandomizableContainer.setBlockEntityLootTable(world, random, mut, lootTable);
                    } else if (FellPondFeature.isInsideEllipse(x, z, semiMajorSq, semiMinorSq, 0)) {
                        setBlock(world, mut, ice.value().getState(world, random, mut));
                    }
                }
            }
        }

        return true;
    }

    private static float getWidthAdjustmentFromProgress(float progress) {
        float coefficient = progress < 0.6f ? 0.6666f : -2.5f;
        return coefficient * (progress - 0.6f) + 1f;
    }

    private static boolean hasSolidTop(WorldGenLevel world, BlockPos pos) {
        return world.isStateAtPosition(pos, state -> {
            VoxelShape shape = state.getCollisionShape(world, pos);
            return Block.isFaceFull(shape, Direction.UP);
        });
    }
}

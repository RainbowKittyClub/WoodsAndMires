package juuxel.woodsandmires.data.builtin;

import juuxel.woodsandmires.WoodsAndMires;
import juuxel.woodsandmires.block.WamBlocks;
import juuxel.woodsandmires.feature.*;
import juuxel.woodsandmires.tree.*;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.TreePlacements;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.BlockBlobFeature;
import net.minecraft.world.level.levelgen.feature.RandomSelectorFeature;
import net.minecraft.world.level.levelgen.feature.SimpleBlockFeature;
import net.minecraft.world.level.levelgen.feature.SimpleRandomSelectorFeature;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.VegetationPatchFeature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedStateProvider;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.data.worldgen.BlockStateProviders;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.core.HolderGetter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.WeightedList;
import net.minecraft.core.Direction;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.PineFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.AlterGroundDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.ForkingTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.GiantTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;

import java.util.ArrayList;
import java.util.List;

public final class WamConfiguredFeatures {
    private final BootstrapContext<Feature> registerable;
    private final HolderGetter<PlacedFeature> placedFeatures;

    // Individual features
    private Holder<Feature> noPodzolPine;

    private WamConfiguredFeatures(BootstrapContext<Feature> registerable) {
        this.registerable = registerable;
        this.placedFeatures = registerable.lookup(Registries.PLACED_FEATURE);
    }

    private void registerGeneral(BootstrapContext<Feature> context) {
        Holder<BlockStateProvider> belowTrunkProvider = context.lookup(Registries.BLOCK_STATE_PROVIDER).getOrThrow(BlockStateProviders.SOIL_BENEATH_TREE);

        register(WamConfiguredFeatureKeys.SHORT_PINE_SHRUB, new ShrubFeature(
                WamBlocks.PINE_LOG.defaultBlockState(),
                WamBlocks.PINE_LEAVES.defaultBlockState(),
                1, 2, 0.6f
            )
        );
        var thinPineShrub = register(WamConfiguredFeatureKeys.THIN_PINE_SHRUB, new ShrubFeature(
                WamBlocks.PINE_SHRUB_LOG.defaultBlockState(),
                WamBlocks.PINE_LEAVES.defaultBlockState(),
                1, 2, 0.8f
            )
        );
        var pine = register(WamConfiguredFeatureKeys.PINE, pineTree(1, 1, belowTrunkProvider));
        var giantPine = register(WamConfiguredFeatureKeys.GIANT_PINE, new TreeFeature.Builder(
                BlockStateProvider.of(WamBlocks.PINE_LOG.defaultBlockState()),
                new GiantTrunkPlacer(10, 4, 2),
                BlockStateProvider.of(WamBlocks.PINE_LEAVES.defaultBlockState()),
                new PineFoliagePlacer(
                    ConstantInt.of(1),
                    ConstantInt.of(1),
                    UniformInt.of(3, 5)
                ),
                new TwoLayersFeatureSize(2, 0, 2),
                belowTrunkProvider
            )
                .ignoreVines()
                .decorators(
                    List.of(
                        new AgedTrunkTreeDecorator(WamBlocks.AGED_PINE_LOG, UniformFloat.of(0.5f, 0.85f)),
                        new AlterGroundDecorator(Holder.direct(
                            RuleBasedStateProvider.ifTrueThenProvide(BlockPredicate.matchesTag(BlockTags.BENEATH_TREE_PODZOL_REPLACEABLE),
                                new WeightedStateProvider(
                                    WeightedList.<BlockState>builder()
                                        .add(Blocks.GRASS_BLOCK.defaultBlockState(), 1)
                                        .add(Blocks.PODZOL.defaultBlockState(), 1)
                                )
                            )))
                    )
                )
                .build()
        );
        var pineSnag = register(WamConfiguredFeatureKeys.PINE_SNAG, new TreeFeature.Builder(
                BlockStateProvider.of(WamBlocks.PINE_SNAG_LOG.defaultBlockState()),
                new ForkingTrunkPlacer(4, 4, 0),
                BlockStateProvider.of(Blocks.AIR.defaultBlockState()),
                new BlobFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0), 0),
                new TwoLayersFeatureSize(2, 0, 2),
                belowTrunkProvider
            )
                .ignoreVines()
                .decorators(List.of(new BranchTreeDecorator(WamBlocks.PINE_SNAG_BRANCH, 0.2f)))
                .build()
        );
        register(WamConfiguredFeatureKeys.PLAINS_FLOWERS, new SimpleRandomSelectorFeature(
                HolderSet.direct(
                    createFlowerPatchFeature(WamBlocks.FIREWEED),
                    createFlowerPatchFeature(WamBlocks.TANSY)
                )
            )
        );
        register(WamConfiguredFeatureKeys.PINE_FROM_SAPLING, new TreeFeature.Builder(
                BlockStateProvider.of(WamBlocks.PINE_LOG.defaultBlockState()),
                new StraightTrunkPlacer(6, 4, 0),
                BlockStateProvider.of(WamBlocks.PINE_LEAVES.defaultBlockState()),
                new PineFoliagePlacer(
                    ConstantInt.of(1),
                    ConstantInt.of(1),
                    UniformInt.of(3, 5)
                ),
                new TwoLayersFeatureSize(2, 0, 2),
                belowTrunkProvider
            ).ignoreVines().decorators(List.of(createPineTrunkDecorator())).build()
        );
        register(WamConfiguredFeatureKeys.PINE_FOREST_BOULDER,
            new BlockBlobFeature(
                Blocks.STONE.defaultBlockState(),
                BlockPredicate.matchesTag(BlockTags.FOREST_ROCK_CAN_PLACE_ON)
            )
        );
        register(
            WamConfiguredFeatureKeys.FOREST_TANSY,
            new SimpleBlockFeature(BlockStateProvider.of(WamBlocks.TANSY))
        );
        register(WamConfiguredFeatureKeys.HEATHER_PATCH, new SimpleBlockFeature(BlockStateProvider.of(WamBlocks.HEATHER))
        );
        var lessPodzolPine = register(WamConfiguredFeatureKeys.LESS_PODZOL_PINE, pineTree(3, 1, belowTrunkProvider));
        noPodzolPine = register(WamConfiguredFeatureKeys.NO_PODZOL_PINE, pineTree(1, 0, belowTrunkProvider));
        register(WamConfiguredFeatureKeys.LUSH_PINE_FOREST_TREES, new RandomSelectorFeature(
                List.of(
                    new WeightedPlacedFeature(
                        PlacementUtils.inlinePlaced(
                            thinPineShrub,
                            PlacementUtils.filteredByBlockSurvival(WamBlocks.PINE_SAPLING)
                        ),
                        0.12f
                    ),
                    new WeightedPlacedFeature(placedFeatures.getOrThrow(TreePlacements.BIRCH_BEES_0002_PLACED), 0.1f),
                    new WeightedPlacedFeature(placedFeatures.getOrThrow(TreePlacements.OAK_BEES_0002_LEAF_LITTER), 0.1f),
                    new WeightedPlacedFeature(placedFeatures.getOrThrow(TreePlacements.FANCY_OAK_BEES_0002_LEAF_LITTER), 0.1f)
                ),
                PlacementUtils.inlinePlaced(lessPodzolPine, PlacementUtils.filteredByBlockSurvival(WamBlocks.PINE_SAPLING))
            )
        );
        register(WamConfiguredFeatureKeys.FALLEN_PINE, new FallenLogFeature(
                WamBlocks.PINE_LOG,
                WamBlocks.AGED_PINE_LOG,
                UniformFloat.of(-0.2f, 1.2f),
                UniformInt.of(2, 6),
                new WeightedStateProvider(
                    WeightedList.<BlockState>builder()
                        .add(Blocks.AIR.defaultBlockState(), 3)
                        .add(Blocks.BROWN_MUSHROOM.defaultBlockState(), 1)
                        .add(Blocks.RED_MUSHROOM.defaultBlockState(), 1)
                        .build()
                )
            )
        );
        register(WamConfiguredFeatureKeys.OLD_GROWTH_PINE_FOREST_TREES, new RandomSelectorFeature(
                List.of(
                    new WeightedPlacedFeature(
                        PlacementUtils.inlinePlaced(
                            pine,
                            PlacementUtils.filteredByBlockSurvival(WamBlocks.PINE_SAPLING)
                        ),
                        0.15f
                    ),
                    new WeightedPlacedFeature(
                        PlacementUtils.inlinePlaced(
                            pineSnag,
                            PlacementUtils.filteredByBlockSurvival(WamBlocks.PINE_SAPLING)
                        ),
                        0.1f
                    )
                ),
                PlacementUtils.inlinePlaced(giantPine, PlacementUtils.filteredByBlockSurvival(WamBlocks.PINE_SAPLING))
            )
        );
        register(WamConfiguredFeatureKeys.SNOWY_PINE_FOREST_TREES, new RandomSelectorFeature(
                List.of(
                    new WeightedPlacedFeature(placedFeatures.getOrThrow(TreePlacements.PINE_ON_SNOW), 0.1f)
                ),
                PlacementUtils.inlinePlaced(pine, getTreeOnSnowPlacementModifiers())
            )
        );
    }

    private static TreeDecorator createPineTrunkDecorator() {
        WeightedList.Builder<TreeDecorator> trunkDecorators = WeightedList.builder();
        trunkDecorators.add(
            new ChanceTreeDecorator(
                new AgedTrunkTreeDecorator(WamBlocks.AGED_PINE_LOG, UniformFloat.of(0.3f, 0.65f)),
                0.95
            ),
            14
        );
        trunkDecorators.add(new ReplaceTrunkTreeDecorator(BlockStateProvider.of(WamBlocks.AGED_PINE_LOG)), 1);
        return new PoolTreeDecorator(trunkDecorators.build());
    }

    private static TreeFeature pineTree(int grassWeight, int podzolWeight, Holder<BlockStateProvider> belowTrunkProvider) {
        List<TreeDecorator> decorators = new ArrayList<>();
        decorators.add(createPineTrunkDecorator());

        if (podzolWeight > 0) {
            decorators.add(
                new AlterGroundDecorator(Holder.direct(
                    RuleBasedStateProvider.ifTrueThenProvide(
                        BlockPredicate.matchesTag(BlockTags.BENEATH_TREE_PODZOL_REPLACEABLE),
                        new WeightedStateProvider(
                            WeightedList.<BlockState>builder()
                                .add(Blocks.GRASS_BLOCK.defaultBlockState(), grassWeight)
                                .add(Blocks.PODZOL.defaultBlockState(), podzolWeight)
                        )
                    )
                ))
            );
        }

        return new TreeFeature.Builder(
            BlockStateProvider.of(WamBlocks.PINE_LOG.defaultBlockState()),
            new StraightTrunkPlacer(6, 4, 0),
            BlockStateProvider.of(WamBlocks.PINE_LEAVES.defaultBlockState()),
            new PineFoliagePlacer(
                ConstantInt.of(1),
                ConstantInt.of(1),
                UniformInt.of(3, 5)
            ),
            new TwoLayersFeatureSize(2, 0, 2),
            belowTrunkProvider
        )
            .ignoreVines()
            .decorators(List.copyOf(decorators))
            .build();
    }

    private static PlacementModifier[] getTreeOnSnowPlacementModifiers() {
        return new PlacementModifier[]{
            EnvironmentScanPlacement.scanningFor(
                Direction.UP,
                BlockPredicate.not(BlockPredicate.matchesBlocks(Blocks.POWDER_SNOW)),
                8
            ),
            BlockPredicateFilter.forPredicate(
                BlockPredicate.matchesBlocks(Direction.DOWN,
                    Blocks.SNOW_BLOCK, Blocks.POWDER_SNOW)
            )
        };
    }

    private void registerMires() {
        register(WamConfiguredFeatureKeys.MIRE_PONDS, new MirePondsFeature());
        register(WamConfiguredFeatureKeys.MIRE_FLOWERS, new SimpleBlockFeature(
                new WeightedStateProvider(
                    WeightedList.<BlockState>builder()
                        .add(Blocks.BLUE_ORCHID.defaultBlockState(), 1)
                        .add(WamBlocks.TANSY.defaultBlockState(), 1)
                )
            )
        );
        register(WamConfiguredFeatureKeys.MIRE_MEADOW, new MeadowFeature(
                new WeightedStateProvider(
                    WeightedList.<BlockState>builder()
                        .add(Blocks.SHORT_GRASS.defaultBlockState(), 5)
                        .add(Blocks.FERN.defaultBlockState(), 1)
                ),
                0.5f
            )
        );
    }

    // Fells
    public static final ResourceKey<LootTable> FROZEN_TREASURE_LOOT_TABLE = ResourceKey.create(Registries.LOOT_TABLE, WoodsAndMires.id("chests/frozen_treasure"));

    private void registerFells(BootstrapContext<Feature> context) {
        HolderGetter<Block> blocks = context.lookup(Registries.BLOCK);

        register(WamConfiguredFeatureKeys.FELL_VEGETATION, new MeadowFeature(
                BlockStateProvider.of(Blocks.SHORT_GRASS),
                0.3f
            )
        );
        register(
            WamConfiguredFeatureKeys.FELL_BOULDER,
            new BlockBlobFeature(
                Blocks.COBBLESTONE.defaultBlockState(),
                BlockPredicate.matchesTag(BlockTags.FOREST_ROCK_CAN_PLACE_ON)
            )
        );
        register(WamConfiguredFeatureKeys.FELL_POND, new FellPondFeature.Builder()
                .radius(UniformInt.of(2, 5))
                .depth(UniformInt.of(1, 4))
                .fillWith(BlockStateProvider.of(Blocks.WATER))
                .border(BlockStateProvider.of(Blocks.STONE))
                .bottomBlock(
                    new WeightedStateProvider(
                        WeightedList.<BlockState>builder()
                            .add(Blocks.EMERALD_ORE.defaultBlockState(), 1)
                            .add(Blocks.GOLD_ORE.defaultBlockState(), 1)
                    ),
                    0.08f
                )
                .build()
        );
        register(WamConfiguredFeatureKeys.FELL_BIRCH_SHRUB, new ShrubFeature(
                Blocks.BIRCH_LOG.defaultBlockState(),
                Blocks.BIRCH_LEAVES.defaultBlockState(),
                1, 1, 0.7f
            )
        );
        register(
            WamConfiguredFeatureKeys.FELL_LICHEN,
            new SimpleBlockFeature(BlockStateProvider.of(WamBlocks.FELL_LICHEN))
        );
        var fellMossPatchVegetation = register(WamConfiguredFeatureKeys.FELL_MOSS_PATCH_VEGETATION, new SimpleBlockFeature(
                new WeightedStateProvider(
                    new WeightedList.Builder<BlockState>()
                        .add(Blocks.MOSS_CARPET.defaultBlockState(), 5)
                        .add(WamBlocks.FELL_LICHEN.defaultBlockState(), 2)
                        .add(WamBlocks.HEATHER.defaultBlockState(), 1)
                )
            )
        );
        register(WamConfiguredFeatureKeys.FELL_MOSS_PATCH, new VegetationPatchFeature(
                blocks.getOrThrow(BlockTags.MOSS_REPLACEABLE),
                BlockStateProvider.holderOf(Blocks.MOSS_BLOCK),
                PlacementUtils.inlinePlaced(fellMossPatchVegetation),
                CaveSurface.FLOOR,
                ConstantInt.of(1),
                0f,
                5,
                0.75f,
                UniformInt.of(2, 4),
                0.3f
            )
        );
        register(WamConfiguredFeatureKeys.FROZEN_TREASURE, new FrozenTreasureFeature(
                new WeightedStateProvider(
                    WeightedList.<BlockState>builder()
                        .add(Blocks.PACKED_ICE.defaultBlockState(), 3)
                        .add(Blocks.BLUE_ICE.defaultBlockState(), 1)
                ),
                UniformInt.of(5, 7),
                ConstantInt.of(2),
                FROZEN_TREASURE_LOOT_TABLE
            )
        );
    }

    private void registerGroves() {
        register(WamConfiguredFeatureKeys.PINY_GROVE_TREES, new RandomSelectorFeature(
                List.of(
                    new WeightedPlacedFeature(placedFeatures.getOrThrow(TreePlacements.PINE_ON_SNOW), 0.1f),
                    new WeightedPlacedFeature(placedFeatures.getOrThrow(TreePlacements.SPRUCE_ON_SNOW), 0.1f)
                ),
                PlacementUtils.inlinePlaced(noPodzolPine, getTreeOnSnowPlacementModifiers())
            )
        );
    }

    public static void register(BootstrapContext<Feature> context) {
        WamConfiguredFeatures configuredFeatures = new WamConfiguredFeatures(context);
        configuredFeatures.registerGeneral(context);
        configuredFeatures.registerMires();
        configuredFeatures.registerFells(context);
        configuredFeatures.registerGroves();
    }

    private Holder<Feature> register(ResourceKey<Feature> key, Feature feature) {
        return registerable.register(key, feature);
    }

    private static Holder<PlacedFeature> createFlowerPatchFeature(Block block) {
        return PlacementUtils.inlinePlaced(
            new SimpleBlockFeature(BlockStateProvider.of(block)),
            CountPlacement.of(64),
            OffsetPlacement.ofTriangle(7, 3),
            PlacementUtils.isEmpty()
        );
    }
}

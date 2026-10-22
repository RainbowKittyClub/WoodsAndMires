package juuxel.woodsandmires.block;

import juuxel.woodsandmires.feature.WamConfiguredFeatureKeys;
import juuxel.woodsandmires.references.WamBlockIds;
import juuxel.woodsandmires.references.WamBlockItemIds;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.item.v1.BlockTransformerHelper;
import net.minecraft.world.level.block.entity.BlockEntityTypes;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;
import net.minecraft.references.BlockItemId;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.block.*;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Function;

public final class WamBlocks {
    public static final Block PINE_LOG = register(WamBlockItemIds.PINE_LOG, copyWoodSettings(Blocks.OAK_LOG), RotatedPillarBlock::new, fuel(ContextIntProviders.COOKING_TIME_WOOD_BLOCKS));
    public static final Block AGED_PINE_LOG = register(WamBlockItemIds.AGED_PINE_LOG, BlockBehaviour.Properties.ofFullCopy(PINE_LOG), s -> new AgedLogBlock(PINE_LOG, s), fuel(ContextIntProviders.COOKING_TIME_WOOD_BLOCKS));
    public static final Block PINE_PLANKS = register(WamBlockItemIds.PINE_PLANKS, copyWoodSettings(Blocks.OAK_PLANKS), Block::new, fuel(ContextIntProviders.COOKING_TIME_WOOD_BLOCKS));
    public static final Block PINE_SLAB = register(WamBlockItemIds.PINE_SLAB, copyWoodSettings(Blocks.OAK_SLAB), SlabBlock::new, fuel(ContextIntProviders.COOKING_TIME_WOOD_SLABS));
    public static final Block PINE_STAIRS = register(WamBlockItemIds.PINE_STAIRS, copyWoodSettings(Blocks.OAK_STAIRS), settings -> new StairBlock(PINE_PLANKS.defaultBlockState(), settings), fuel(ContextIntProviders.COOKING_TIME_WOOD_BLOCKS));
    public static final Block PINE_FENCE = register(WamBlockItemIds.PINE_FENCE, copyWoodSettings(Blocks.OAK_FENCE), FenceBlock::new, fuel(ContextIntProviders.COOKING_TIME_WOOD_BLOCKS));
    public static final Block PINE_FENCE_GATE = register(WamBlockItemIds.PINE_FENCE_GATE, copyWoodSettings(Blocks.OAK_FENCE_GATE), settings -> new FenceGateBlock(WamWoodTypes.PINE, settings));
    public static final Block PINE_DOOR = register(WamBlockItemIds.PINE_DOOR, copyWoodSettings(Blocks.OAK_DOOR), settings -> new DoorBlock(WamBlockSetTypes.PINE, settings), fuel(DoubleHighBlockItem::new, ContextIntProviders.COOKING_TIME_WOOD_ITEMS_LARGE));
    public static final Block PINE_TRAPDOOR = register(WamBlockItemIds.PINE_TRAPDOOR, copyWoodSettings(Blocks.OAK_TRAPDOOR), settings -> new TrapDoorBlock(WamBlockSetTypes.PINE, settings), fuel(ContextIntProviders.COOKING_TIME_WOOD_BLOCKS));
    public static final Block PINE_BUTTON = register(WamBlockItemIds.PINE_BUTTON, copyWoodSettings(Blocks.OAK_BUTTON), settings -> new ButtonBlock(WamBlockSetTypes.PINE, 30, settings), fuel(ContextIntProviders.COOKING_TIME_WOOD_ITEMS_EXTRA_SMALL));
    public static final Block PINE_PRESSURE_PLATE = register(WamBlockItemIds.PINE_PRESSURE_PLATE, copyWoodSettings(Blocks.OAK_PRESSURE_PLATE), settings -> new PressurePlateBlock(WamBlockSetTypes.PINE, settings), fuel(ContextIntProviders.COOKING_TIME_WOOD_BLOCKS));
    public static final Block PINE_SIGN = register(WamBlockItemIds.PINE_SIGN, copyWoodSettings(Blocks.OAK_SIGN), settings -> new StandingSignBlock(WamWoodTypes.PINE, settings), null);
    public static final Block PINE_WALL_SIGN = register(WamBlockIds.PINE_WALL_SIGN, copyWoodSettings(Blocks.OAK_WALL_SIGN).overrideLootTable(PINE_SIGN.getLootTable()).overrideDescription(PINE_SIGN.getDescriptionId()), settings -> new WallSignBlock(WamWoodTypes.PINE, settings));
    public static final Block PINE_HANGING_SIGN = register(WamBlockItemIds.PINE_HANGING_SIGN, copyWoodSettings(Blocks.OAK_HANGING_SIGN), settings -> new CeilingHangingSignBlock(WamWoodTypes.PINE, settings), null);
    public static final Block PINE_WALL_HANGING_SIGN = register(WamBlockIds.PINE_WALL_HANGING_SIGN, copyWoodSettings(Blocks.OAK_WALL_HANGING_SIGN).overrideLootTable(PINE_HANGING_SIGN.getLootTable()).overrideDescription(PINE_HANGING_SIGN.getDescriptionId()), settings -> new WallHangingSignBlock(WamWoodTypes.PINE, settings));
    // Supplier for same reason as above
    public static final Block PINE_LEAVES = register(WamBlockItemIds.PINE_LEAVES, Blocks.leavesProperties(SoundType.GRASS), s -> new TintedParticleLeavesBlock(0.01f, s));
    public static final Block PINE_SAPLING = register(WamBlockItemIds.PINE_SAPLING, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING), settings -> new SaplingBlock(new TreeGrower("pine", WeightedList.of(WamConfiguredFeatureKeys.PINE_FROM_SAPLING), WeightedList.of(), WeightedList.of(), WamConfiguredFeatureKeys.PINE_FROM_SAPLING), settings), fuel(ContextIntProviders.COOKING_TIME_DRY_PLANTS));
    public static final Block POTTED_PINE_SAPLING = register(WamBlockIds.POTTED_PINE_SAPLING, Blocks.flowerPotProperties(), settings -> new FlowerPotBlock(PINE_SAPLING, settings));
    public static final Block PINE_WOOD = register(WamBlockItemIds.PINE_WOOD, copyWoodSettings(Blocks.OAK_WOOD), RotatedPillarBlock::new, fuel(ContextIntProviders.COOKING_TIME_WOOD_BLOCKS));
    public static final Block AGED_PINE_WOOD = register(WamBlockItemIds.AGED_PINE_WOOD, BlockBehaviour.Properties.ofFullCopy(PINE_WOOD)
        .overrideDescription(PINE_WOOD.getDescriptionId()), RotatedPillarBlock::new, fuel(ContextIntProviders.COOKING_TIME_WOOD_BLOCKS));
    public static final Block STRIPPED_PINE_LOG = register(WamBlockItemIds.STRIPPED_PINE_LOG, copyWoodSettings(Blocks.STRIPPED_OAK_LOG), RotatedPillarBlock::new, fuel(ContextIntProviders.COOKING_TIME_WOOD_BLOCKS));
    public static final Block STRIPPED_PINE_WOOD = register(WamBlockItemIds.STRIPPED_PINE_WOOD, copyWoodSettings(Blocks.STRIPPED_OAK_WOOD), RotatedPillarBlock::new, fuel(ContextIntProviders.COOKING_TIME_WOOD_BLOCKS));
    public static final Block PINE_SNAG_LOG = register(WamBlockItemIds.PINE_SNAG_LOG, copyWoodSettings(Blocks.STRIPPED_OAK_LOG), RotatedPillarBlock::new, fuel(ContextIntProviders.COOKING_TIME_WOOD_BLOCKS));
    public static final Block PINE_SNAG_WOOD = register(WamBlockItemIds.PINE_SNAG_WOOD, copyWoodSettings(Blocks.STRIPPED_OAK_WOOD), RotatedPillarBlock::new, fuel(ContextIntProviders.COOKING_TIME_WOOD_BLOCKS));
    public static final Block PINE_SNAG_BRANCH = register(WamBlockIds.PINE_SNAG_BRANCH, copyWoodSettings(PINE_SNAG_WOOD), BranchBlock::new);
    public static final Block PINE_SHRUB_LOG = register(WamBlockItemIds.PINE_SHRUB_LOG, copyWoodSettings(PINE_SNAG_WOOD).noOcclusion(), settings -> new ShrubLogBlock(settings, PINE_LEAVES), fuel(ContextIntProviders.COOKING_TIME_WOOD_BLOCKS));
    public static final Block FIREWEED = register(WamBlockItemIds.FIREWEED, createFlowerSettings(), TallFlowerBlock::new, DoubleHighBlockItem::new);
    public static final Block TANSY = register(WamBlockItemIds.TANSY, createFlowerSettings(), settings -> new BigFlowerBlock(MobEffects.SLOW_FALLING, 10, settings));
    public static final Block POTTED_TANSY = register(WamBlockIds.POTTED_TANSY, Blocks.flowerPotProperties(), settings -> new FlowerPotBlock(TANSY, settings));
    public static final Block FELL_LICHEN = register(WamBlockItemIds.FELL_LICHEN, createFlowerSettings().mapColor(MapColor.QUARTZ).offsetType(BlockBehaviour.OffsetType.XZ), LichenBlock::new);
    public static final Block POTTED_FELL_LICHEN = register(WamBlockIds.POTTED_FELL_LICHEN, Blocks.flowerPotProperties(), settings -> new FlowerPotBlock(FELL_LICHEN, settings));
    public static final Block HEATHER = register(WamBlockItemIds.HEATHER, createFlowerSettings(), settings -> new HeatherBlock(MobEffects.REGENERATION, 8, settings));
    public static final Block POTTED_HEATHER = register(WamBlockIds.POTTED_HEATHER, Blocks.flowerPotProperties(), settings -> new FlowerPotBlock(HEATHER, settings));

    private WamBlocks() {
    }

    public static void init() {
        FlammableBlockRegistry fbr = FlammableBlockRegistry.getDefaultInstance();
        fbr.add(PINE_LOG, 5, 5);
        fbr.add(AGED_PINE_LOG, 5, 5);
        fbr.add(PINE_WOOD, 5, 5);
        fbr.add(AGED_PINE_WOOD, 5, 5);
        fbr.add(STRIPPED_PINE_LOG, 5, 5);
        fbr.add(STRIPPED_PINE_WOOD, 5, 5);
        fbr.add(PINE_SNAG_LOG, 5, 5);
        fbr.add(PINE_SNAG_WOOD, 5, 5);
        fbr.add(PINE_LEAVES, 5, 20);
        // Vanilla's odds for oak's planks and the blocks made from them (FireBlock#bootStrap).
        fbr.add(PINE_PLANKS, 5, 20);
        fbr.add(PINE_SLAB, 5, 20);
        fbr.add(PINE_STAIRS, 5, 20);
        fbr.add(PINE_FENCE, 5, 20);
        fbr.add(PINE_FENCE_GATE, 5, 20);

        BlockTransformerHelper.registerStripping(PINE_LOG, STRIPPED_PINE_LOG);
        BlockTransformerHelper.registerStripping(AGED_PINE_LOG, STRIPPED_PINE_LOG);
        BlockTransformerHelper.registerStripping(PINE_WOOD, STRIPPED_PINE_WOOD);
        BlockTransformerHelper.registerStripping(AGED_PINE_WOOD, STRIPPED_PINE_WOOD);
        BlockEntityTypes.SIGN.addValidBlock(PINE_SIGN);
        BlockEntityTypes.SIGN.addValidBlock(PINE_WALL_SIGN);
        BlockEntityTypes.HANGING_SIGN.addValidBlock(PINE_HANGING_SIGN);
        BlockEntityTypes.HANGING_SIGN.addValidBlock(PINE_WALL_HANGING_SIGN);
    }

    private static Block register(BlockItemId id, Function<BlockBehaviour.Properties, Block> block, @Nullable BiFunction<Block, Item.Properties, Item> item) {
        return register(id, BlockBehaviour.Properties.of(), block, item);
    }

    private static Block register(BlockItemId id, BlockBehaviour.Properties settings, Function<BlockBehaviour.Properties, Block> block) {
        return register(id, settings, block, BlockItem::new);
    }

    private static Block register(ResourceKey<Block> id, BlockBehaviour.Properties settings, Function<BlockBehaviour.Properties, Block> block) {
        var val = block.apply(settings.setId(id));
        return Registry.register(BuiltInRegistries.BLOCK, id, val);
    }

    private static Block register(BlockItemId id, BlockBehaviour.Properties settings, Function<BlockBehaviour.Properties, Block> block, @Nullable BiFunction<Block, Item.Properties, Item> item) {
        var val = Registry.register(BuiltInRegistries.BLOCK, id.block(), block.apply(settings.setId(id.block())));

        if (item != null) {
            Item registeredItem = Registry.register(BuiltInRegistries.ITEM, id.item(), item.apply(val, new Item.Properties()
                .setId(id.item())
                .overrideDescription(val.getDescriptionId())
            ));

            if (registeredItem instanceof BlockItem blockItem) {
                blockItem.registerBlocks(Item.BY_BLOCK, registeredItem);
            }
        }

        return val;
    }

    private static BiFunction<Block, Item.Properties, Item> fuel(ResourceKey<ContextIntProvider> cookingTime) {
        return fuel(BlockItem::new, cookingTime);
    }

    private static BiFunction<Block, Item.Properties, Item> fuel(BiFunction<Block, Item.Properties, Item> item, ResourceKey<ContextIntProvider> cookingTime) {
        return (block, settings) -> item.apply(block, settings.cookingFuel(cookingTime));
    }

    private static BlockBehaviour.Properties copyWoodSettings(Block block) {
        return BlockBehaviour.Properties.ofFullCopy(block);
    }

    private static BlockBehaviour.Properties createFlowerSettings() {
        return BlockBehaviour.Properties.of()
            .mapColor(MapColor.PLANT)
            .noCollision()
            .instabreak()
            .pushReaction(PushReaction.POPPED)
            .sound(SoundType.GRASS);
    }
}

package juuxel.woodsandmires.data;

import juuxel.woodsandmires.block.WamBlockTags;
import juuxel.woodsandmires.references.WamBlockIds;
import juuxel.woodsandmires.references.WamBlockItemIds;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public final class WamBlockTagProvider extends FabricTagsProvider.BlockTagsProvider {
    public WamBlockTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        // Minecraft tags
        builder(BlockTags.MINEABLE_WITH_AXE)
            .add(WamBlockIds.PINE_SNAG_BRANCH);
        builder(BlockTags.CEILING_HANGING_SIGNS)
            .add(WamBlockItemIds.PINE_HANGING_SIGN.block());
        builder(BlockTags.FENCE_GATES)
            .add(WamBlockItemIds.PINE_FENCE_GATE.block());
        builder(BlockTags.FLOWER_POTS)
            .add(WamBlockIds.POTTED_PINE_SAPLING)
            .add(WamBlockIds.POTTED_TANSY)
            .add(WamBlockIds.POTTED_FELL_LICHEN)
            .add(WamBlockIds.POTTED_HEATHER);
        builder(BlockTags.LEAVES)
            .add(WamBlockItemIds.PINE_LEAVES.block());
        builder(BlockTags.LOGS)
            .addTag(WamBlockTags.PINE_LOGS);
        builder(BlockTags.PLANKS)
            .add(WamBlockItemIds.PINE_PLANKS.block());
        builder(BlockTags.SMALL_FLOWERS)
            .add(WamBlockItemIds.HEATHER.block(), WamBlockItemIds.TANSY.block());
        builder(BlockTags.STANDING_SIGNS)
            .add(WamBlockItemIds.PINE_SIGN.block());
        builder(BlockTags.BEE_ATTRACTIVE)
            .add(WamBlockItemIds.FIREWEED.block());
        builder(BlockTags.WALL_HANGING_SIGNS)
            .add(WamBlockIds.PINE_WALL_HANGING_SIGN);
        builder(BlockTags.WALL_SIGNS)
            .add(WamBlockIds.PINE_WALL_SIGN);
        builder(BlockTags.WOODEN_BUTTONS)
            .add(WamBlockItemIds.PINE_BUTTON.block());
        builder(BlockTags.WOODEN_DOORS)
            .add(WamBlockItemIds.PINE_DOOR.block());
        builder(BlockTags.WOODEN_FENCES)
            .add(WamBlockItemIds.PINE_FENCE.block());
        builder(BlockTags.WOODEN_PRESSURE_PLATES)
            .add(WamBlockItemIds.PINE_PRESSURE_PLATE.block());
        builder(BlockTags.WOODEN_SLABS)
            .add(WamBlockItemIds.PINE_SLAB.block());
        builder(BlockTags.WOODEN_STAIRS)
            .add(WamBlockItemIds.PINE_STAIRS.block());
        builder(BlockTags.WOODEN_TRAPDOORS)
            .add(WamBlockItemIds.PINE_TRAPDOOR.block());

        // WaM tags
        builder(WamBlockTags.LICHEN_PLANTABLE_ON)
            .forceAddTag(BlockTags.DIRT)
            .add(Blocks.FARMLAND.builtInRegistryHolder().key())
            .forceAddTag(BlockTags.BASE_STONE_OVERWORLD);
        builder(WamBlockTags.PINE_LOGS)
            .addTag(WamBlockTags.THICK_PINE_LOGS)
            .add(WamBlockItemIds.PINE_SHRUB_LOG.block());
        builder(WamBlockTags.THICK_PINE_LOGS)
            .add(WamBlockItemIds.PINE_LOG.block(), WamBlockItemIds.AGED_PINE_LOG.block())
            .add(WamBlockItemIds.PINE_WOOD.block(), WamBlockItemIds.AGED_PINE_WOOD.block())
            .add(WamBlockItemIds.STRIPPED_PINE_LOG.block(), WamBlockItemIds.STRIPPED_PINE_WOOD.block())
            .add(WamBlockItemIds.PINE_SNAG_LOG.block(), WamBlockItemIds.PINE_SNAG_WOOD.block());
    }
}

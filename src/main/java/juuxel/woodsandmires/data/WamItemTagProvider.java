package juuxel.woodsandmires.data;

import juuxel.woodsandmires.data.builtin.CommonItemTags;
import juuxel.woodsandmires.references.WamBlockItemIds;
import juuxel.woodsandmires.references.WamItemIds;
import juuxel.woodsandmires.item.WamItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;

import java.util.concurrent.CompletableFuture;

public final class WamItemTagProvider extends FabricTagsProvider.ItemTagsProvider {
    public WamItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture, null);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        // Minecraft tags
        builder(ItemTags.BOATS)
            .add(WamItemIds.PINE_BOAT);
        builder(ItemTags.CHEST_BOATS)
            .add(WamItemIds.PINE_CHEST_BOAT);
        builder(ItemTags.HANGING_SIGNS)
            .add(WamBlockItemIds.PINE_HANGING_SIGN.item());
        builder(ItemTags.LEAVES)
            .add(WamBlockItemIds.PINE_LEAVES.item());
        builder(ItemTags.LOGS_THAT_BURN)
            .addTag(WamItemTags.PINE_LOGS);
        builder(ItemTags.PLANKS)
            .add(WamBlockItemIds.PINE_PLANKS.item());
        builder(ItemTags.SAPLINGS)
            .add(WamBlockItemIds.PINE_SAPLING.item());
        builder(ItemTags.SIGNS)
            .add(WamBlockItemIds.PINE_SIGN.item());
        builder(ItemTags.BEE_FOOD)
            .add(WamBlockItemIds.FIREWEED.item());
        builder(ItemTags.WOODEN_BUTTONS)
            .add(WamBlockItemIds.PINE_BUTTON.item());
        builder(ItemTags.WOODEN_DOORS)
            .add(WamBlockItemIds.PINE_DOOR.item());
        builder(ItemTags.WOODEN_FENCES)
            .add(WamBlockItemIds.PINE_FENCE.item());
        builder(ItemTags.WOODEN_PRESSURE_PLATES)
            .add(WamBlockItemIds.PINE_PRESSURE_PLATE.item());
        builder(ItemTags.WOODEN_SLABS)
            .add(WamBlockItemIds.PINE_SLAB.item());
        builder(ItemTags.WOODEN_STAIRS)
            .add(WamBlockItemIds.PINE_STAIRS.item());
        builder(ItemTags.WOODEN_TRAPDOORS)
            .add(WamBlockItemIds.PINE_TRAPDOOR.item());

        // WaM tags
        builder(WamItemTags.PINE_LOGS)
            .addTag(WamItemTags.THICK_PINE_LOGS)
            .add(WamBlockItemIds.PINE_SHRUB_LOG.item());
        builder(WamItemTags.THICK_PINE_LOGS)
            .add(WamBlockItemIds.PINE_LOG.item(), WamBlockItemIds.AGED_PINE_LOG.item())
            .add(WamBlockItemIds.PINE_WOOD.item(), WamBlockItemIds.AGED_PINE_WOOD.item())
            .add(WamBlockItemIds.STRIPPED_PINE_LOG.item(), WamBlockItemIds.STRIPPED_PINE_WOOD.item())
            .add(WamBlockItemIds.PINE_SNAG_LOG.item(), WamBlockItemIds.PINE_SNAG_WOOD.item());

        // Common tags

        TagAppender<Item> chains = builder(CommonItemTags.CHAINS);
        chains.add(key(Items.IRON_CHAIN));
        Items.COPPER_CHAIN.forEach(item -> chains.add(key(item)));
        builder(CommonItemTags.HONEY)
            .add(WamItemIds.PINE_CONE_JAM);
        builder(CommonItemTags.JAMS)
            .add(WamItemIds.PINE_CONE_JAM);
        builder(CommonItemTags.PINE_CONES)
            .add(WamItemIds.PINE_CONE);
        builder(CommonItemTags.SUGAR)
            .add(key(Items.SUGAR));
        builder(CommonItemTags.WOODEN_CHESTS)
            .add(key(Items.CHEST), key(Items.TRAPPED_CHEST));
        builder(CommonItemTags.WOODEN_RODS)
            .add(key(Items.STICK));
    }

    private static ResourceKey<Item> key(Item item) {
        return item.builtInRegistryHolder().key();
    }
}

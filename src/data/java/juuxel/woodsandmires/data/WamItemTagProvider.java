package juuxel.woodsandmires.data;

import juuxel.woodsandmires.block.WamBlocks;
import juuxel.woodsandmires.data.builtin.CommonItemTags;
import juuxel.woodsandmires.item.WamItemTags;
import juuxel.woodsandmires.item.WamItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;

import java.util.concurrent.CompletableFuture;

public final class WamItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public WamItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture, null);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        // Minecraft tags
        valueLookupBuilder(ItemTags.LEAVES)
            .add(WamBlocks.PINE_LEAVES.asItem());
        valueLookupBuilder(ItemTags.LOGS_THAT_BURN)
            .addTag(WamItemTags.PINE_LOGS);
        valueLookupBuilder(ItemTags.SAPLINGS)
            .add(WamBlocks.PINE_SAPLING.asItem());
        valueLookupBuilder(ItemTags.SMALL_FLOWERS)
            .add(WamBlocks.HEATHER.asItem(), WamBlocks.TANSY.asItem());
        valueLookupBuilder(ItemTags.BEE_FOOD)
            .add(WamBlocks.FIREWEED.asItem());

        // WaM tags
        valueLookupBuilder(WamItemTags.PINE_LOGS)
            .addTag(WamItemTags.THICK_PINE_LOGS);
        valueLookupBuilder(WamItemTags.THICK_PINE_LOGS)
            .add(WamBlocks.PINE_LOG.asItem(), WamBlocks.AGED_PINE_LOG.asItem())
            .add(WamBlocks.PINE_WOOD.asItem(), WamBlocks.AGED_PINE_WOOD.asItem())
            .add(WamBlocks.STRIPPED_PINE_LOG.asItem(), WamBlocks.STRIPPED_PINE_WOOD.asItem())
            .add(WamBlocks.PINE_SNAG_LOG.asItem(), WamBlocks.PINE_SNAG_WOOD.asItem());

        // Common tags
        valueLookupBuilder(CommonItemTags.CHAINS)
            .add(Items.CHAIN);
        valueLookupBuilder(CommonItemTags.HONEY)
            .add(WamItems.PINE_CONE_JAM);
        valueLookupBuilder(CommonItemTags.JAMS)
            .add(WamItems.PINE_CONE_JAM);
        valueLookupBuilder(CommonItemTags.PINE_CONES)
            .add(WamItems.PINE_CONE);
        valueLookupBuilder(CommonItemTags.SUGAR)
            .add(Items.SUGAR);
        valueLookupBuilder(CommonItemTags.WOODEN_CHESTS)
            .add(Items.CHEST, Items.TRAPPED_CHEST);
        valueLookupBuilder(CommonItemTags.WOODEN_RODS)
            .add(Items.STICK);
    }
    
}

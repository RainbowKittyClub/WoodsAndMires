package juuxel.woodsandmires.data;

import juuxel.woodsandmires.data.builtin.WamConfiguredFeatures;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableSubProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.TagEntry;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public final class WamChestLootTableProvider extends SimpleFabricLootTableSubProvider {
    private final CompletableFuture<HolderLookup.Provider> registryLookup;

    public WamChestLootTableProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup, LootContextParamSets.CHEST);
        this.registryLookup = registryLookup;
    }

    @Override
    public void run() {
        // Fabric's datagen calls generate(BiConsumer) instead.
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> sink) {
        // Fabric only calls this once the lookup is done.
        HolderGetter<Item> items = registryLookup.join().lookupOrThrow(Registries.ITEM);
        sink.accept(WamConfiguredFeatures.FROZEN_TREASURE_LOOT_TABLE,
            new LootTable.Builder()
                .withPool(
                    LootPool.lootPool()
                        .setRolls(ContextIntProviders.between(2, 4))
                        .add(TagEntry.expandTag(items.getOrThrow(ConventionalItemTags.IRON_INGOTS)))
                        .add(TagEntry.expandTag(items.getOrThrow(ConventionalItemTags.GOLD_INGOTS)))
                        .add(TagEntry.expandTag(items.getOrThrow(ConventionalItemTags.COPPER_INGOTS)))
                )
                .withPool(
                    LootPool.lootPool()
                        .setRolls(ContextIntProviders.between(1, 2))
                        .add(TagEntry.expandTag(items.getOrThrow(ConventionalItemTags.EMERALD_GEMS)))
                        .add(LootItem.lootTableItem(Items.NAME_TAG))
                )
                .withPool(
                    LootPool.lootPool()
                        .setRolls(ContextIntProviders.exactly(4))
                        .add(LootItem.lootTableItem(Items.STRING))
                        .add(LootItem.lootTableItem(Items.SPIDER_EYE))
                        .add(LootItem.lootTableItem(Items.ROTTEN_FLESH))
                        .add(LootItem.lootTableItem(Items.BONE))
                )
        );
    }
}

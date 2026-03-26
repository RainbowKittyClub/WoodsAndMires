package juuxel.woodsandmires.item;

import juuxel.woodsandmires.WoodsAndMires;
import juuxel.woodsandmires.block.WamBlocks;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTabOutput;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.*;
import java.util.function.Predicate;

public final class WamItemGroups {
    private static final ResourceKey<CreativeModeTab> ITEM_GROUP = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(WoodsAndMires.ID, "items"));

    public static void init() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.BUILDING_BLOCKS).register(entries -> {
            entries.insertAfter(Items.WARPED_BUTTON,
                WamBlocks.PINE_LOG,
                WamBlocks.AGED_PINE_LOG,
                WamBlocks.PINE_SHRUB_LOG,
                WamBlocks.PINE_WOOD,
                WamBlocks.AGED_PINE_WOOD,
                WamBlocks.STRIPPED_PINE_LOG,
                WamBlocks.STRIPPED_PINE_WOOD,
                WamBlocks.PINE_SNAG_LOG,
                WamBlocks.PINE_SNAG_WOOD,
                WamBlocks.PINE_PLANKS,
                WamBlocks.PINE_STAIRS,
                WamBlocks.PINE_SLAB,
                WamBlocks.PINE_FENCE,
                WamBlocks.PINE_FENCE_GATE,
                WamBlocks.PINE_DOOR,
                WamBlocks.PINE_TRAPDOOR,
                WamBlocks.PINE_PRESSURE_PLATE,
                WamBlocks.PINE_BUTTON
            );
        });
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.NATURAL_BLOCKS).register(entries -> {
            entries.insertAfter(Items.WARPED_STEM,
                WamBlocks.PINE_LOG,
                WamBlocks.AGED_PINE_LOG,
                WamBlocks.PINE_SHRUB_LOG,
                WamBlocks.PINE_SNAG_LOG);
            entries.insertAfter(Items.FLOWERING_AZALEA_LEAVES,
                WamBlocks.PINE_LEAVES);
            entries.insertAfter(Items.FLOWERING_AZALEA,
                WamBlocks.PINE_SAPLING);
            entries.insertAfter(Items.LILY_OF_THE_VALLEY,
                WamBlocks.TANSY,
                WamBlocks.HEATHER);
            entries.insertAfter(Items.PEONY,
                WamBlocks.FIREWEED);
            entries.insertBefore(Items.GLOW_LICHEN,
                WamBlocks.FELL_LICHEN);
        });
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(entries -> {
            entries.insertAfter(Items.WARPED_HANGING_SIGN,
                WamBlocks.PINE_SIGN,
                WamBlocks.PINE_HANGING_SIGN);
        });
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(entries -> {
            entries.insertAfter(Items.BAMBOO_CHEST_RAFT,
                WamItems.PINE_BOAT,
                WamItems.PINE_CHEST_BOAT);
        });
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register(entries -> {
            addBefore(entries, stack -> stack.is(Items.ENCHANTED_BOOK),
                WamItems.PINE_CONE);
        });
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FOOD_AND_DRINKS).register(entries -> {
            entries.accept(WamItems.PINE_CONE_JAM);
        });

        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, ITEM_GROUP, FabricCreativeModeTab.builder()
            .title(Component.literal("Woods and Mires"))
            .icon(() -> WamBlocks.PINE_SAPLING.asItem().getDefaultInstance())
            .displayItems((context, entries) -> {
                BuiltInRegistries.ITEM.listElementIds().filter(itemRegistryKey -> itemRegistryKey.identifier().getNamespace().equals(WoodsAndMires.ID)).map(BuiltInRegistries.ITEM::getValue).forEach(entries::accept);
            }).build()
        );
    }

    private static void addBefore(FabricCreativeModeTabOutput entries, Predicate<ItemStack> predicate, ItemLike... items) {
        var stacks = Arrays.stream(items).map(ItemStack::new).toList();
        entries.insertBefore(predicate, stacks, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
    }

    private static void insertAfterFirstEnabled(FabricCreativeModeTabOutput entries, List<Item> after, ItemLike... items) {
        Item start = after.stream()
            .filter(item -> item.requiredFeatures().isSubsetOf(entries.getEnabledFeatures()))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Could not find any of the items " + after));
        entries.insertAfter(start, items);
    }
}

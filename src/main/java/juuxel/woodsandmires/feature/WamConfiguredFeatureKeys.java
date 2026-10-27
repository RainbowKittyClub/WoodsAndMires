package juuxel.woodsandmires.feature;

import juuxel.woodsandmires.WoodsAndMires;
import juuxel.woodsandmires.util.RegistryCollector;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;

public final class WamConfiguredFeatureKeys {
    public static final RegistryCollector<ResourceKey<Feature>> ALL = new RegistryCollector<>();

    // General
    public static final ResourceKey<Feature> SHORT_PINE_SHRUB = key("short_pine_shrub");
    public static final ResourceKey<Feature> THIN_PINE_SHRUB = key("thin_pine_shrub");
    public static final ResourceKey<Feature> PINE = key("pine");
    public static final ResourceKey<Feature> GIANT_PINE = key("giant_pine");
    public static final ResourceKey<Feature> PINE_SNAG = key("pine_snag");
    public static final ResourceKey<Feature> PLAINS_FLOWERS = key("plains_flowers");
    public static final ResourceKey<Feature> PINE_FROM_SAPLING = key("pine_from_sapling");
    public static final ResourceKey<Feature> PINE_FOREST_BOULDER = key("pine_forest_boulder");
    public static final ResourceKey<Feature> FOREST_TANSY = key("forest_tansy");
    public static final ResourceKey<Feature> HEATHER_PATCH = key("heather_patch");
    public static final ResourceKey<Feature> LESS_PODZOL_PINE = key("less_podzol_pine");
    public static final ResourceKey<Feature> NO_PODZOL_PINE = key("no_podzol_pine");
    public static final ResourceKey<Feature> LUSH_PINE_FOREST_TREES = key("lush_pine_forest_trees");
    public static final ResourceKey<Feature> FALLEN_PINE = key("fallen_pine");
    public static final ResourceKey<Feature> OLD_GROWTH_PINE_FOREST_TREES = key("old_growth_pine_forest_trees");
    public static final ResourceKey<Feature> SNOWY_PINE_FOREST_TREES = key("snowy_pine_forest_trees");

    // Mire
    public static final ResourceKey<Feature> MIRE_PONDS = key("mire_ponds");
    public static final ResourceKey<Feature> MIRE_FLOWERS = key("mire_flowers");
    public static final ResourceKey<Feature> MIRE_MEADOW = key("mire_meadow");

    // Fells
    public static final ResourceKey<Feature> FELL_VEGETATION = key("fell_vegetation");
    public static final ResourceKey<Feature> FELL_BOULDER = key("fell_boulder");
    public static final ResourceKey<Feature> FELL_POND = key("fell_pond");
    public static final ResourceKey<Feature> FELL_BIRCH_SHRUB = key("fell_birch_shrub");
    public static final ResourceKey<Feature> FELL_LICHEN = key("fell_lichen");
    public static final ResourceKey<Feature> FELL_MOSS_PATCH_VEGETATION = key("fell_moss_patch_vegetation");
    public static final ResourceKey<Feature> FELL_MOSS_PATCH = key("fell_moss_patch");
    public static final ResourceKey<Feature> FROZEN_TREASURE = key("frozen_treasure");

    // Groves
    public static final ResourceKey<Feature> PINY_GROVE_TREES = key("piny_grove_trees");

    private static ResourceKey<Feature> key(String id) {
        return ALL.add(ResourceKey.create(Registries.FEATURE, WoodsAndMires.id(id)));
    }
}

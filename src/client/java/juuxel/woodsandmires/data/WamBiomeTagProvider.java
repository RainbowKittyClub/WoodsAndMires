package juuxel.woodsandmires.data;

import juuxel.woodsandmires.biome.WamBiomeKeys;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import java.util.concurrent.CompletableFuture;

public final class WamBiomeTagProvider extends FabricTagsProvider<Biome> {
    public WamBiomeTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.BIOME, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        // Vanilla tags
        generateOverworld(BiomeTags.IS_OVERWORLD);
        builder(BiomeTags.IS_FOREST)
            .add(WamBiomeKeys.PINE_FOREST)
            .add(WamBiomeKeys.OLD_GROWTH_PINE_FOREST)
            .add(WamBiomeKeys.SNOWY_PINE_FOREST)
            .add(WamBiomeKeys.LUSH_PINE_FOREST)
            .add(WamBiomeKeys.PINY_GROVE);
        builder(BiomeTags.IS_HILL)
            .add(WamBiomeKeys.SNOWY_PINE_FOREST);
        builder(BiomeTags.IS_MOUNTAIN)
            .add(WamBiomeKeys.FELL)
            .add(WamBiomeKeys.SNOWY_FELL);
        builder(BiomeTags.IS_TAIGA)
            .add(WamBiomeKeys.PINE_FOREST)
            .add(WamBiomeKeys.OLD_GROWTH_PINE_FOREST)
            .add(WamBiomeKeys.SNOWY_PINE_FOREST);
        generateOverworld(BiomeTags.HAS_STRONGHOLD);
        builder(BiomeTags.HAS_PILLAGER_OUTPOST)
            .add(WamBiomeKeys.PINE_FOREST)
            .add(WamBiomeKeys.PINY_GROVE);

        // Common tags
        generateOverworld(ConventionalBiomeTags.IS_OVERWORLD);
        builder(ConventionalBiomeTags.IS_COLD_OVERWORLD)
            .add(WamBiomeKeys.PINE_FOREST)
            .add(WamBiomeKeys.SNOWY_PINE_FOREST)
            .add(WamBiomeKeys.OLD_GROWTH_PINE_FOREST)
            .add(WamBiomeKeys.FELL)
            .add(WamBiomeKeys.SNOWY_FELL)
            .add(WamBiomeKeys.PINY_GROVE);
        builder(ConventionalBiomeTags.IS_TEMPERATE_OVERWORLD)
            .add(WamBiomeKeys.LUSH_PINE_FOREST);
        builder(ConventionalBiomeTags.IS_WET_OVERWORLD)
            .add(WamBiomeKeys.PINE_MIRE);
        builder(ConventionalBiomeTags.IS_SWAMP)
            .add(WamBiomeKeys.PINE_MIRE);
        builder(ConventionalBiomeTags.IS_HILL)
            .add(WamBiomeKeys.SNOWY_PINE_FOREST)
            .add(WamBiomeKeys.FELL)
            .add(WamBiomeKeys.SNOWY_FELL);
        builder(ConventionalBiomeTags.IS_MOUNTAIN_PEAK)
            .add(WamBiomeKeys.FELL)
            .add(WamBiomeKeys.SNOWY_FELL);
        builder(ConventionalBiomeTags.IS_MOUNTAIN_SLOPE)
            .add(WamBiomeKeys.SNOWY_PINE_FOREST);
        builder(ConventionalBiomeTags.IS_SNOWY)
            .add(WamBiomeKeys.SNOWY_PINE_FOREST)
            .add(WamBiomeKeys.SNOWY_FELL)
            .add(WamBiomeKeys.PINY_GROVE);
    }

    private void generateOverworld(TagKey<Biome> tag) {
        var tagBuilder = builder(tag);
        WamBiomeKeys.ALL.stream().forEach(tagBuilder::add);
    }
}

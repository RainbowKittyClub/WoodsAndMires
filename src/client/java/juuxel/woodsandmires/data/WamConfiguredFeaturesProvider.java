package juuxel.woodsandmires.data;

import juuxel.woodsandmires.feature.WamConfiguredFeatureKeys;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.levelgen.feature.Feature;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public final class WamConfiguredFeaturesProvider extends EncoderBasedDataProvider<Feature> {
    public WamConfiguredFeaturesProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture, Registries.FEATURE);
    }

    @Override
    protected Stream<ResourceKey<Feature>> getEntries() {
        return WamConfiguredFeatureKeys.ALL.stream();
    }

    @Override
    public String getName() {
        return "Configured Features";
    }
}

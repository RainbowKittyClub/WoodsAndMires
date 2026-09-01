package juuxel.woodsandmires.data;

import juuxel.woodsandmires.biome.WamBiomeKeys;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.biome.Biome;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public final class WamBiomeProvider extends EncoderBasedDataProvider<Biome> {
    public WamBiomeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture, Registries.BIOME);
    }

    @Override
    protected Stream<ResourceKey<Biome>> getEntries() {
        return WamBiomeKeys.ALL.stream();
    }

    @Override
    public String getName() {
        return "Biomes";
    }
}

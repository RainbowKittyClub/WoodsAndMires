package juuxel.woodsandmires.terrablender;

import juuxel.woodsandmires.WoodsAndMires;
import juuxel.woodsandmires.biome.WamBiomeKeys;
import juuxel.woodsandmires.config.WamConfig;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.material.MaterialRules;
import net.minecraft.world.level.levelgen.material.condition.MaterialCondition;
import net.minecraft.world.level.levelgen.material.rule.MaterialRule;
import net.minecraft.world.level.levelgen.placement.CaveSurface;
import terrablender.api.MaterialRuleManager;
import terrablender.api.Regions;
import terrablender.api.TerraBlenderApi;

public final class WoodsAndMiresTb implements TerraBlenderApi {
    @Override
    public void onTerraBlenderInitialized() {
        WamConfig.load();
        Regions.register(new WamRegion(WoodsAndMires.id("biomes"), WamConfig.biomeRegionWeight));
        MaterialRuleManager.addRules(MaterialRuleManager.RuleCategory.OVERWORLD, WoodsAndMires.ID, WoodsAndMiresTb::createSurfaceRule);
    }

    private static MaterialRule createSurfaceRule(RegistryAccess registries) {
        HolderGetter<Biome> biomes = registries.lookupOrThrow(Registries.BIOME);
        MaterialCondition hasWater = MaterialRules.waterBlockCheck(0, 0);
        MaterialRule stone = makeStateRule(Blocks.STONE);
        MaterialRule snowBlock = makeStateRule(Blocks.SNOW_BLOCK);
        MaterialRule powderSnow = MaterialRules.ifTrue(
            MaterialRules.noiseCondition2d(Noises.POWDER_SNOW, 0.35, 0.6),
            MaterialRules.ifTrue(hasWater, makeStateRule(Blocks.POWDER_SNOW))
        );

        return MaterialRules.ifTrue(MaterialRules.stoneDepthCheck(0, false, CaveSurface.FLOOR), MaterialRules.sequence(
            MaterialRules.ifTrue(
                MaterialRules.isBiome(biomes, WamBiomeKeys.FELL),
                MaterialRules.ifTrue(surfaceNoiseAbove(1.75), stone)
            ),
            MaterialRules.ifTrue(
                MaterialRules.isBiome(biomes, WamBiomeKeys.OLD_GROWTH_PINE_FOREST),
                MaterialRules.ifTrue(
                    surfaceNoiseAbove(1.75),
                    makeStateRule(Blocks.COARSE_DIRT)
                )
            ),
            MaterialRules.ifTrue(
                MaterialRules.isBiome(biomes, WamBiomeKeys.PINY_GROVE),
                MaterialRules.sequence(
                    powderSnow,
                    MaterialRules.ifTrue(hasWater, snowBlock),
                    makeStateRule(Blocks.DIRT)
                )
            ),
            MaterialRules.ifTrue(
                MaterialRules.isBiome(biomes, WamBiomeKeys.SNOWY_PINE_FOREST),
                MaterialRules.sequence(
                    MaterialRules.ifTrue(MaterialRules.steep(), stone),
                    powderSnow,
                    MaterialRules.ifTrue(hasWater, snowBlock)
                )
            ),
            MaterialRules.ifTrue(
                MaterialRules.isBiome(biomes, WamBiomeKeys.SNOWY_FELL),
                MaterialRules.sequence(
                    MaterialRules.ifTrue(surfaceNoiseAbove(1.75), stone),
                    MaterialRules.ifTrue(MaterialRules.steep(), stone),
                    MaterialRules.ifTrue(hasWater, snowBlock)
                )
            )
        ));
    }

    private static MaterialRule makeStateRule(Block block) {
        return MaterialRules.state(block.defaultBlockState());
    }

    // Vanilla's private OverworldMaterialRules.surfaceNoiseAbove.
    private static MaterialCondition surfaceNoiseAbove(double threshold) {
        return MaterialRules.noiseCondition2d(Noises.SURFACE, threshold / 8.25, Double.MAX_VALUE);
    }
}

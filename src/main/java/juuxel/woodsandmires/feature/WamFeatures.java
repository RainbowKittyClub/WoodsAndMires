package juuxel.woodsandmires.feature;

import com.mojang.serialization.MapCodec;
import juuxel.woodsandmires.WoodsAndMires;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.feature.Feature;

public final class WamFeatures {
    public static final MapCodec<ShrubFeature> SHRUB = ShrubFeature.CODEC;
    public static final MapCodec<MirePondsFeature> MIRE_PONDS = MirePondsFeature.CODEC;
    public static final MapCodec<MeadowFeature> MEADOW = MeadowFeature.CODEC;
    public static final MapCodec<FellPondFeature> FELL_POND = FellPondFeature.CODEC;
    public static final MapCodec<FallenLogFeature> FALLEN_LOG = FallenLogFeature.CODEC;
    public static final MapCodec<FrozenTreasureFeature> FROZEN_TREASURE = FrozenTreasureFeature.CODEC;

    private WamFeatures() {
    }

    public static void init() {
        register("shrub", SHRUB);
        register("mire_ponds", MIRE_PONDS);
        register("meadow", MEADOW);
        register("fell_pond", FELL_POND);
        register("fallen_log", FALLEN_LOG);
        register("frozen_treasure", FROZEN_TREASURE);
    }

    private static void register(String id, MapCodec<? extends Feature> codec) {
        Registry.register(BuiltInRegistries.FEATURE_TYPE, WoodsAndMires.id(id), codec);
    }
}

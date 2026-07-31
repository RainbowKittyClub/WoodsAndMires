package juuxel.woodsandmires.references;

import juuxel.woodsandmires.WoodsAndMires;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;

public final class WamBlockIds {
    public static final ResourceKey<Block> PINE_WALL_SIGN = create("pine_wall_sign");
    public static final ResourceKey<Block> PINE_WALL_HANGING_SIGN = create("pine_wall_hanging_sign");
    public static final ResourceKey<Block> POTTED_PINE_SAPLING = create("potted_pine_sapling");
    public static final ResourceKey<Block> PINE_SNAG_BRANCH = create("pine_snag_branch");
    public static final ResourceKey<Block> POTTED_TANSY = create("potted_tansy");
    public static final ResourceKey<Block> POTTED_FELL_LICHEN = create("potted_fell_lichen");
    public static final ResourceKey<Block> POTTED_HEATHER = create("potted_heather");

    private WamBlockIds() {
    }

    private static ResourceKey<Block> create(String name) {
        return ResourceKey.create(Registries.BLOCK, WoodsAndMires.id(name));
    }
}

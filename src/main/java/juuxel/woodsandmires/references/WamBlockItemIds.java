package juuxel.woodsandmires.references;

import juuxel.woodsandmires.WoodsAndMires;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.Identifier;

public final class WamBlockItemIds {
    public static final BlockItemId PINE_LOG = create("pine_log");
    public static final BlockItemId AGED_PINE_LOG = create("aged_pine_log");
    public static final BlockItemId PINE_PLANKS = create("pine_planks");
    public static final BlockItemId PINE_SLAB = create("pine_slab");
    public static final BlockItemId PINE_STAIRS = create("pine_stairs");
    public static final BlockItemId PINE_FENCE = create("pine_fence");
    public static final BlockItemId PINE_FENCE_GATE = create("pine_fence_gate");
    public static final BlockItemId PINE_DOOR = create("pine_door");
    public static final BlockItemId PINE_TRAPDOOR = create("pine_trapdoor");
    public static final BlockItemId PINE_BUTTON = create("pine_button");
    public static final BlockItemId PINE_PRESSURE_PLATE = create("pine_pressure_plate");
    public static final BlockItemId PINE_SIGN = create("pine_sign");
    public static final BlockItemId PINE_HANGING_SIGN = create("pine_hanging_sign");
    public static final BlockItemId PINE_LEAVES = create("pine_leaves");
    public static final BlockItemId PINE_SAPLING = create("pine_sapling");
    public static final BlockItemId PINE_WOOD = create("pine_wood");
    public static final BlockItemId AGED_PINE_WOOD = create("aged_pine_wood");
    public static final BlockItemId STRIPPED_PINE_LOG = create("stripped_pine_log");
    public static final BlockItemId STRIPPED_PINE_WOOD = create("stripped_pine_wood");
    public static final BlockItemId PINE_SNAG_LOG = create("pine_snag_log");
    public static final BlockItemId PINE_SNAG_WOOD = create("pine_snag_wood");
    public static final BlockItemId PINE_SHRUB_LOG = create("pine_shrub_log");
    public static final BlockItemId FIREWEED = create("fireweed");
    public static final BlockItemId TANSY = create("tansy");
    public static final BlockItemId FELL_LICHEN = create("fell_lichen");
    public static final BlockItemId HEATHER = create("heather");

    private WamBlockItemIds() {
    }

    private static BlockItemId create(String name) {
        Identifier id = WoodsAndMires.id(name);
        return BlockItemId.create(id, id);
    }
}

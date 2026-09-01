package juuxel.woodsandmires.references;

import juuxel.woodsandmires.WoodsAndMires;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public final class WamItemIds {
    public static final ResourceKey<Item> PINE_BOAT = create("pine_boat");
    public static final ResourceKey<Item> PINE_CHEST_BOAT = create("pine_chest_boat");
    public static final ResourceKey<Item> PINE_CONE = create("pine_cone");
    public static final ResourceKey<Item> PINE_CONE_JAM = create("pine_cone_jam");

    private WamItemIds() {
    }

    private static ResourceKey<Item> create(String name) {
        return ResourceKey.create(Registries.ITEM, WoodsAndMires.id(name));
    }
}

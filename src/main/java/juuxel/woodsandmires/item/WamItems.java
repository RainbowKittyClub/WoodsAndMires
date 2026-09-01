package juuxel.woodsandmires.item;

import com.terraformersmc.terraform.boat.api.item.TerraformBoatItemHelper;
import juuxel.woodsandmires.WoodsAndMires;
import juuxel.woodsandmires.block.WamBlocks;
import juuxel.woodsandmires.references.WamBlockItemIds;
import juuxel.woodsandmires.references.WamItemIds;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;

import java.util.function.Function;

public final class WamItems {
    public static final Item PINE_BOAT = TerraformBoatItemHelper.registerBoatItem(Identifier.fromNamespaceAndPath(WoodsAndMires.ID, "pine"), false);
    public static final Item PINE_CHEST_BOAT = TerraformBoatItemHelper.registerBoatItem(Identifier.fromNamespaceAndPath(WoodsAndMires.ID, "pine"), true);
    public static final Item PINE_CONE = register(WamItemIds.PINE_CONE, new Item.Properties());
    public static final Item PINE_CONE_JAM = register(WamItemIds.PINE_CONE_JAM, new Item.Properties()
        .craftRemainder(Items.GLASS_BOTTLE)
        .usingConvertsTo(Items.GLASS_BOTTLE)
        .food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.25f).build())
    );
    public static final Item SIGN = register(WamBlockItemIds.PINE_SIGN.item(), settings -> new SignItem(WamBlocks.PINE_SIGN, WamBlocks.PINE_WALL_SIGN, settings), new Item.Properties().stacksTo(16).useBlockDescriptionPrefix());
    public static final Item HANGING_SIGN = register(WamBlockItemIds.PINE_HANGING_SIGN.item(), settings -> new HangingSignItem(WamBlocks.PINE_HANGING_SIGN, WamBlocks.PINE_WALL_HANGING_SIGN, settings), new Item.Properties().stacksTo(16).useBlockDescriptionPrefix());

    public static void init() {
    }

    private static Item register(ResourceKey<Item> id, Function<Item.Properties, Item> function, Item.Properties settings) {
        return Registry.register(BuiltInRegistries.ITEM, id, function.apply(settings.setId(id)));
    }

    private static Item register(ResourceKey<Item> id, Item.Properties settings) {
        return register(id, Item::new, settings);
    }
}

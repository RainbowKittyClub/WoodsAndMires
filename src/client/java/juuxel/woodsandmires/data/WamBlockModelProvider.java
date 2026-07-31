package juuxel.woodsandmires.data;

import juuxel.woodsandmires.WoodsAndMires;
import juuxel.woodsandmires.block.AgedLogBlock;
import juuxel.woodsandmires.block.BranchBlock;
import juuxel.woodsandmires.block.ShrubLogBlock;
import juuxel.woodsandmires.block.WamBlocks;
import juuxel.woodsandmires.item.WamItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.color.item.GrassColorSource;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.ConditionBuilder;
import net.minecraft.client.data.models.blockstates.MultiPartGenerator;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.client.renderer.item.ClientItem;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

import java.util.Optional;
import java.util.function.BiConsumer;

public final class WamBlockModelProvider extends FabricModelProvider {
    private static final int PINE_LEAVES_COLOR = -12012264;
    private static final TextureSlot DECORATION = TextureSlot.create("decoration");
    private static final TextureSlot FLOWER = TextureSlot.create("flower");
    private static final ModelTemplate LAYERED_CROSS = new ModelTemplate(
        Optional.of(WoodsAndMires.id("block/templates/layered_cross")),
        Optional.empty(),
        TextureSlot.CROSS, DECORATION
    );
    private static final ModelTemplate LAYERED_MIRRORED_CROSS = new ModelTemplate(
        Optional.of(WoodsAndMires.id("block/templates/layered_mirrored_cross")),
        Optional.empty(),
        TextureSlot.CROSS, DECORATION
    );
    private static final ModelTemplate POTTED_LAYERED_CROSS = new ModelTemplate(
        Optional.of(WoodsAndMires.id("block/templates/potted_layered_cross")),
        Optional.empty(),
        TextureSlot.PLANT, FLOWER
    );
    private static final ModelTemplate SHRUB_LOG = new ModelTemplate(
        Optional.of(WoodsAndMires.id("block/templates/shrub_log")),
        Optional.empty(),
        TextureSlot.SIDE, TextureSlot.END, TextureSlot.PARTICLE
    );
    private static final ModelTemplate BRANCH_THIN = new ModelTemplate(
        Optional.of(WoodsAndMires.id("block/templates/branch_thin")),
        Optional.empty(),
        TextureSlot.SIDE, TextureSlot.END, TextureSlot.PARTICLE
    );
    private static final ModelTemplate BRANCH_THICK = new ModelTemplate(
        Optional.of(WoodsAndMires.id("block/templates/branch_thick")),
        Optional.empty(),
        TextureSlot.SIDE, TextureSlot.END, TextureSlot.PARTICLE
    );

    public WamBlockModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {
        blockModelGenerators.family(WamBlocks.PINE_PLANKS).generateFor(WamRecipeProvider.PINE_FAMILY);
        blockModelGenerators.woodProvider(WamBlocks.PINE_LOG).log(WamBlocks.PINE_LOG).wood(WamBlocks.PINE_WOOD);
        blockModelGenerators.woodProvider(WamBlocks.STRIPPED_PINE_LOG).log(WamBlocks.STRIPPED_PINE_LOG).wood(WamBlocks.STRIPPED_PINE_WOOD);
        blockModelGenerators.woodProvider(WamBlocks.PINE_SNAG_LOG).log(WamBlocks.PINE_SNAG_LOG).wood(WamBlocks.PINE_SNAG_WOOD);
        blockModelGenerators.createTintedLeaves(WamBlocks.PINE_LEAVES, net.minecraft.client.data.models.model.TexturedModel.LEAVES, PINE_LEAVES_COLOR);
        blockModelGenerators.createPlantWithDefaultItem(WamBlocks.PINE_SAPLING, WamBlocks.POTTED_PINE_SAPLING, BlockModelGenerators.PlantType.NOT_TINTED);
        blockModelGenerators.createPlantWithDefaultItem(WamBlocks.FELL_LICHEN, WamBlocks.POTTED_FELL_LICHEN, BlockModelGenerators.PlantType.NOT_TINTED);
        blockModelGenerators.createPlantWithDefaultItem(WamBlocks.HEATHER, WamBlocks.POTTED_HEATHER, BlockModelGenerators.PlantType.NOT_TINTED);

        registerAgedPineLog(blockModelGenerators);
        registerPineShrubLog(blockModelGenerators);
        registerPineSnagBranch(blockModelGenerators);
        registerFireweed(blockModelGenerators);
        registerTansy(blockModelGenerators);
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators) {
        ItemModelOutput itemModelOutput = itemModelGenerators.itemModelOutput;
        itemModelGenerators.generateFlatItem(WamItems.PINE_BOAT, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(WamItems.PINE_CHEST_BOAT, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(WamItems.PINE_CONE, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(WamItems.PINE_CONE_JAM, ModelTemplates.FLAT_ITEM);
        registerBlockItemModel(itemModelOutput, WamBlocks.AGED_PINE_LOG.asItem(), WoodsAndMires.id("block/pine_log_aged"));
        registerBlockItemModel(itemModelOutput, WamBlocks.AGED_PINE_WOOD.asItem());
        registerBlockItemModel(itemModelOutput, WamBlocks.PINE_SHRUB_LOG.asItem());
        registerLayeredTintedItemModel(itemModelGenerators, WamBlocks.FIREWEED.asItem(), itemTexture("fireweed_base"), itemTexture("fireweed_flowers"));
        registerLayeredTintedItemModel(itemModelGenerators, WamBlocks.TANSY.asItem(), texture("tansy_base"), texture("tansy_flowers"));
    }

    private static void registerAgedPineLog(BlockModelGenerators blockModelGenerators) {
        BiConsumer<Identifier, ModelInstance> modelOutput = blockModelGenerators.modelOutput;
        Identifier agedModel = ModelTemplates.CUBE_COLUMN.create(WoodsAndMires.id("block/pine_log_aged"), TextureMapping.column(
            texture("pine_log_aged"),
            texture("pine_log_top_aged")
        ), modelOutput);
        Identifier midModel = ModelTemplates.CUBE_BOTTOM_TOP.create(WoodsAndMires.id("block/pine_log_mid"), new TextureMapping()
            .put(TextureSlot.SIDE, texture("pine_log_mid"))
            .put(TextureSlot.BOTTOM, texture("pine_log_top_aged"))
            .put(TextureSlot.TOP, TextureMapping.getBlockTexture(WamBlocks.PINE_LOG, "_top"))
            .copySlot(TextureSlot.SIDE, TextureSlot.PARTICLE), modelOutput);

        blockModelGenerators.blockStateOutput.accept(MultiVariantGenerator.dispatch(WamBlocks.AGED_PINE_LOG).with(PropertyDispatch.initial(AgedLogBlock.MID, AgedLogBlock.AXIS)
            .generate((mid, axis) -> rotatedPillar(mid ? midModel : agedModel, axis))));

        Identifier agedWoodModel = ModelTemplates.CUBE_COLUMN.create(WoodsAndMires.id("block/aged_pine_wood"), TextureMapping.column(
            texture("pine_log_aged"),
            texture("pine_log_aged")
        ), modelOutput);
        blockModelGenerators.blockStateOutput.accept(MultiVariantGenerator.dispatch(WamBlocks.AGED_PINE_WOOD).with(PropertyDispatch.initial(AgedLogBlock.AXIS)
            .generate(axis -> rotatedPillar(agedWoodModel, axis))));
    }

    private static void registerPineShrubLog(BlockModelGenerators blockModelGenerators) {
        Identifier model = SHRUB_LOG.create(WamBlocks.PINE_SHRUB_LOG, new TextureMapping()
            .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(WamBlocks.PINE_LOG))
            .put(TextureSlot.END, TextureMapping.getBlockTexture(WamBlocks.PINE_LOG, "_top"))
            .put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(WamBlocks.PINE_LOG)), blockModelGenerators.modelOutput);
        blockModelGenerators.blockStateOutput.accept(MultiPartGenerator.multiPart(WamBlocks.PINE_SHRUB_LOG)
            .with(new ConditionBuilder().term(RotatedPillarBlock.AXIS, Direction.Axis.Y), plainVariant(model))
            .with(new ConditionBuilder().term(RotatedPillarBlock.AXIS, Direction.Axis.Z), plainVariant(model).with(BlockModelGenerators.X_ROT_90))
            .with(new ConditionBuilder().term(RotatedPillarBlock.AXIS, Direction.Axis.X), plainVariant(model).with(BlockModelGenerators.X_ROT_90).with(BlockModelGenerators.Y_ROT_90))
            .with(new ConditionBuilder().term(ShrubLogBlock.HAS_LEAVES, true), plainVariant(WoodsAndMires.id("block/pine_leaves"))));
    }

    private static void registerPineSnagBranch(BlockModelGenerators blockModelGenerators) {
        Identifier thin = BRANCH_THIN.create(WoodsAndMires.id("block/pine_snag_branch_thin"), snagBranchTextures(), blockModelGenerators.modelOutput);
        Identifier thick = BRANCH_THICK.create(WoodsAndMires.id("block/pine_snag_branch_thick"), snagBranchTextures(), blockModelGenerators.modelOutput);

        blockModelGenerators.blockStateOutput.accept(MultiVariantGenerator.dispatch(WamBlocks.PINE_SNAG_BRANCH).with(PropertyDispatch.initial(BranchBlock.AXIS, BranchBlock.STYLE)
            .generate((axis, style) -> rotatedHorizontalPillar(style == BranchBlock.Style.THIN ? thin : thick, axis))));
    }

    private static void registerFireweed(BlockModelGenerators blockModelGenerators) {
        Identifier bottom = LAYERED_CROSS.create(WoodsAndMires.id("block/fireweed_bottom"), layeredPlant("fireweed_bottom"), blockModelGenerators.modelOutput);
        Identifier bottomMirrored = LAYERED_MIRRORED_CROSS.create(WoodsAndMires.id("block/fireweed_bottom_mirrored"), layeredPlant("fireweed_bottom"), blockModelGenerators.modelOutput);
        Identifier top = LAYERED_CROSS.create(WoodsAndMires.id("block/fireweed_top"), layeredPlant("fireweed_top"), blockModelGenerators.modelOutput);
        Identifier topMirrored = LAYERED_MIRRORED_CROSS.create(WoodsAndMires.id("block/fireweed_top_mirrored"), layeredPlant("fireweed_top"), blockModelGenerators.modelOutput);

        blockModelGenerators.blockStateOutput.accept(MultiVariantGenerator.dispatch(WamBlocks.FIREWEED).with(PropertyDispatch.initial(DoublePlantBlock.HALF)
            .select(DoubleBlockHalf.LOWER, variants(bottom, bottomMirrored))
            .select(DoubleBlockHalf.UPPER, variants(top, topMirrored))));
    }

    private static void registerTansy(BlockModelGenerators blockModelGenerators) {
        Identifier model = LAYERED_CROSS.create(WamBlocks.TANSY, layeredPlant("tansy"), blockModelGenerators.modelOutput);
        Identifier pottedModel = POTTED_LAYERED_CROSS.create(WamBlocks.POTTED_TANSY, new TextureMapping()
            .put(TextureSlot.PLANT, texture("tansy_base"))
            .put(FLOWER, texture("tansy_flowers")), blockModelGenerators.modelOutput);

        blockModelGenerators.blockStateOutput.accept(MultiVariantGenerator.dispatch(WamBlocks.TANSY, plainVariant(model)));
        blockModelGenerators.blockStateOutput.accept(MultiVariantGenerator.dispatch(WamBlocks.POTTED_TANSY, plainVariant(pottedModel)));
    }

    private static TextureMapping layeredPlant(String id) {
        return new TextureMapping()
            .put(TextureSlot.CROSS, texture(id + "_base"))
            .put(DECORATION, texture(id + "_flowers"));
    }

    private static TextureMapping snagBranchTextures() {
        return new TextureMapping()
            .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(WamBlocks.PINE_SNAG_LOG))
            .put(TextureSlot.END, TextureMapping.getBlockTexture(WamBlocks.PINE_SNAG_LOG, "_top"))
            .put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(WamBlocks.PINE_SNAG_LOG));
    }

    private static MultiVariant rotatedPillar(Identifier model, Direction.Axis axis) {
        return switch (axis) {
            case X -> plainVariant(model).with(BlockModelGenerators.X_ROT_90).with(BlockModelGenerators.Y_ROT_90);
            case Y -> plainVariant(model);
            case Z -> plainVariant(model).with(BlockModelGenerators.X_ROT_90);
        };
    }

    private static MultiVariant rotatedHorizontalPillar(Identifier model, Direction.Axis axis) {
        return switch (axis) {
            case X -> plainVariant(model).with(BlockModelGenerators.X_ROT_90).with(BlockModelGenerators.Y_ROT_90);
            case Z -> plainVariant(model).with(BlockModelGenerators.X_ROT_90);
            default -> throw new IllegalStateException("Unsupported branch axis: " + axis);
        };
    }

    private static MultiVariant plainVariant(Identifier model) {
        return new MultiVariant(WeightedList.of(new Variant(model)));
    }

    private static MultiVariant variants(Identifier... models) {
        Variant[] variants = new Variant[models.length];

        for (int i = 0; i < models.length; i++) {
            variants[i] = new Variant(models[i]);
        }

        return new MultiVariant(WeightedList.of(variants));
    }

    private static void registerBlockItemModel(ItemModelOutput itemModelOutput, Item item) {
        registerBlockItemModel(itemModelOutput, item, BuiltInRegistries.ITEM.getKey(item).withPrefix("block/"));
    }

    private static void registerBlockItemModel(ItemModelOutput itemModelOutput, Item item, Identifier model) {
        itemModelOutput.accept(item, ItemModelUtils.plainModel(model), ClientItem.Properties.DEFAULT);
    }

    private static void registerLayeredTintedItemModel(ItemModelGenerators itemModelGenerators, Item item, Material base, Material overlay) {
        Identifier model = ModelTemplates.TWO_LAYERED_ITEM.create(item, new TextureMapping()
            .put(TextureSlot.LAYER0, base)
            .put(TextureSlot.LAYER1, overlay), itemModelGenerators.modelOutput);
        itemModelGenerators.itemModelOutput.accept(item, ItemModelUtils.tintedModel(model, new GrassColorSource()), ClientItem.Properties.DEFAULT);
    }

    private static Material itemTexture(String id) {
        return new Material(WoodsAndMires.id("item/" + id));
    }

    private static Material texture(String id) {
        return new Material(WoodsAndMires.id("block/" + id));
    }
}

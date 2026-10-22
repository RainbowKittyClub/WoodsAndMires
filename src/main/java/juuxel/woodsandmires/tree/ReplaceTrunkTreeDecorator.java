package juuxel.woodsandmires.tree;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

public final class ReplaceTrunkTreeDecorator extends TreeDecorator {
    public static final MapCodec<ReplaceTrunkTreeDecorator> CODEC =
        RecordCodecBuilder.mapCodec(builder ->
            builder.group(
                BlockStateProvider.CODEC.fieldOf("trunk").forGetter(ReplaceTrunkTreeDecorator::getTrunk)
            ).apply(builder, ReplaceTrunkTreeDecorator::new)
        );
    private final Holder<BlockStateProvider> trunk;

    public ReplaceTrunkTreeDecorator(Holder<BlockStateProvider> trunk) {
        this.trunk = trunk;
    }

    public ReplaceTrunkTreeDecorator(BlockStateProvider trunk) {
        this(Holder.direct(trunk));
    }

    public Holder<BlockStateProvider> getTrunk() {
        return trunk;
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return WamTreeDecorators.REPLACE_TRUNK;
    }

    @Override
    public void place(Context generator) {
        for (BlockPos pos : generator.logs()) {
            // Don't replace the dirt underneath the trunk
            if (generator.level().isStateAtPosition(pos, blockState -> blockState.is(BlockTags.SUBSTRATE_OVERWORLD))) continue;

            generator.setBlock(pos, trunk.value().getState(generator.level(), generator.random(), pos));
        }
    }
}

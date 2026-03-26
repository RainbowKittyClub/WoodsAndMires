package juuxel.woodsandmires.client;

import net.fabricmc.fabric.api.client.particle.v1.ParticleRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;
import net.minecraft.client.color.block.BlockTintSources;

import java.util.List;

import static juuxel.woodsandmires.block.WamBlocks.*;

public final class WamBlocksClient {
    public static void init() {
        BlockColorRegistry.register(
            List.of(BlockTintSources.grass()),
            FIREWEED, TANSY, POTTED_TANSY, PINE_LEAVES, PINE_SHRUB_LOG
        );

        ParticleRenderEvents.ALLOW_TERRAIN_PARTICLE_TINT.register((state, world, pos) -> {
            // Prevent tinting shrub log particles.
            // See https://github.com/Juuxel/WoodsAndMires/issues/5
            return !state.is(PINE_SHRUB_LOG);
        });
    }
}

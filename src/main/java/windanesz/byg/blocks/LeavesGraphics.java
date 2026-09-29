package windanesz.byg.blocks;

import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;

/**
 * Follows the vanilla fast/fancy leaves setting. The client updates it on vanilla leaves when graphics change,
 * so BYG leaves mirror vanilla oak leaves instead of tracking the option themselves.
 */
final class LeavesGraphics {

    private LeavesGraphics() {
    }

    static boolean isFancy() {
        return !Blocks.LEAVES.isOpaqueCube(Blocks.LEAVES.getDefaultState());
    }

    /** Fast graphics renders cutout leaves solid, like vanilla; other layers (e.g. translucent) are kept. */
    static BlockRenderLayer renderLayer(BlockRenderLayer fancyLayer) {
        return fancyLayer == BlockRenderLayer.CUTOUT_MIPPED && !isFancy() ? BlockRenderLayer.SOLID : fancyLayer;
    }

    static boolean isOpaque(BlockRenderLayer fancyLayer) {
        return fancyLayer == BlockRenderLayer.CUTOUT_MIPPED && !isFancy();
    }
}

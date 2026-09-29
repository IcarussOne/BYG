package windanesz.byg.worldgen;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

import java.util.Random;

/**
 * Gate for every BYG tree placed outside the vanilla biome decorator. Fires Forge's TREE decoration event so
 * listeners that replace or cancel trees (e.g. Dynamic Trees) also control BYG's own tree placement.
 */
public final class BygTreePlacement {

    private BygTreePlacement() {
    }

    /**
     * @param pos the tree position, or the chunk decoration origin when checking once for a whole chunk
     * @return false if a listener denied tree generation here
     */
    public static boolean allowTrees(World world, Random random, BlockPos pos) {
        return TerrainGen.decorate(world, random, new ChunkPos(pos), pos, DecorateBiomeEvent.Decorate.EventType.TREE);
    }
}

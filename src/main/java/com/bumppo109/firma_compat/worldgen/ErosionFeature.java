package com.bumppo109.firma_compat.worldgen;

import com.mojang.serialization.Codec;
import net.dries007.tfc.common.entities.misc.TFCFallingBlockEntity;
import net.dries007.tfc.common.recipes.LandslideRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.HashMap;
import java.util.Map;

public class ErosionFeature extends Feature<ErosionFeatureConfig> {

    public ErosionFeature(Codec<ErosionFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<ErosionFeatureConfig> context) {
        WorldGenLevel level = context.level();
        ChunkAccess chunk = level.getChunk(context.origin());
        ChunkPos chunkPos = chunk.getPos();
        int chunkX = chunkPos.getMinBlockX();
        int chunkZ = chunkPos.getMinBlockZ();

        ErosionFeatureConfig config = context.config();

        // Build replacement map: Raw Block → Hardened BlockState (from JSON config)
        Map<Block, BlockState> replacementMap = new HashMap<>();
        for (ErosionFeatureConfig.RawToHardenedPair pair : config.getReplacements()) {
            Block raw = pair.getRawBlock();
            BlockState hardened = pair.getHardenedState();
            if (raw != null && hardened != null) {
                replacementMap.put(raw, hardened);
            }
        }

        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        int minY = context.chunkGenerator().getMinY();

        // Optional: Try to get aquifer if available (for water handling)
        Aquifer aquifer = null;
        // If you want to keep basic aquifer support without full TFC extension:
        // aquifer = ... (we can add later if needed)

        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                int baseHeight = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, chunkX + x, chunkZ + z);

                boolean prevBlockCanLandslide = false;
                int lastSafeY = baseHeight;
                BlockState prevHardenedState = null;   // Now stores full BlockState from config

                mutablePos.set(chunkX + x, baseHeight, chunkZ + z);

                for (int y = baseHeight; y >= minY; --y) {
                    mutablePos.setY(y);
                    BlockState stateAt = chunk.getBlockState(mutablePos);

                    LandslideRecipe recipe = stateAt.isAir() ? null : LandslideRecipe.getRecipe(stateAt);
                    boolean stateAtIsFragile = stateAt.isAir() || TFCFallingBlockEntity.canFallThrough(level, mutablePos, stateAt);

                    // === Original TFC Landslide Logic ===
                    if (prevBlockCanLandslide) {
                        if (recipe == null) {                    // Found stable block after landslide zone
                            if (stateAtIsFragile) {
                                // Place hardened block in the gap
                                BlockState toPlace = getReplacementOrOriginal(stateAt, replacementMap);
                                if (lastSafeY > y + 2) {
                                    mutablePos.setY(y + 1);
                                    setBlock(level, chunk, mutablePos, toPlace);
                                } else {
                                    // Try to respect aquifer (water) if possible
                                    mutablePos.setY(y + 1);
                                    setBlock(level, chunk, mutablePos, toPlace);
                                }
                            }
                            prevBlockCanLandslide = false;
                            lastSafeY = y;
                        }
                    } else if (recipe == null) {
                        lastSafeY = y;
                    } else {
                        prevBlockCanLandslide = true;
                    }

                    // === Gap filling logic (original TFC behavior) ===
                    if (stateAtIsFragile) {
                        if (prevHardenedState != null) {
                            mutablePos.setY(y + 1);
                            setBlock(level, chunk, mutablePos, prevHardenedState);
                        }
                        prevHardenedState = null;
                    } else {
                        // Use configured replacement if available, otherwise keep original
                        prevHardenedState = getReplacementOrOriginal(stateAt, replacementMap);
                    }
                }
            }
        }

        return true;
    }

    /** Returns the hardened replacement if configured, otherwise returns the original state */
    private BlockState getReplacementOrOriginal(BlockState current, Map<Block, BlockState> replacementMap) {
        BlockState replacement = replacementMap.get(current.getBlock());
        return replacement != null ? replacement : current;
    }

    private void setBlock(WorldGenLevel level, ChunkAccess chunk, BlockPos pos, BlockState state) {
        BlockState prevState = chunk.setBlockState(pos, state, false);
        if (prevState != null && prevState.hasBlockEntity()) {
            chunk.removeBlockEntity(pos);
        }
        if (state.hasPostProcess(level, pos)) {
            chunk.markPosForPostprocessing(pos);
        }
    }
}
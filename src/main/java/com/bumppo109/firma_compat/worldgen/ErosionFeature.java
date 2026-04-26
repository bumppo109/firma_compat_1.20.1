package com.bumppo109.firma_compat.worldgen;

import com.mojang.serialization.Codec;
import net.dries007.tfc.common.entities.misc.TFCFallingBlockEntity;
import net.dries007.tfc.common.recipes.LandslideRecipe;
import net.dries007.tfc.world.ChunkGeneratorExtension;
import net.dries007.tfc.world.chunkdata.ChunkDataProvider;
import net.dries007.tfc.world.chunkdata.RockData;
import net.dries007.tfc.world.settings.RockLayerSettings;
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
        BlockPos origin = context.origin();
        ChunkAccess chunk = level.getChunk(origin);
        ChunkPos chunkPos = chunk.getPos();
        int chunkX = chunkPos.getMinBlockX();
        int chunkZ = chunkPos.getMinBlockZ();

        ErosionFeatureConfig config = context.config();

        // Build replacement map: Raw Block → Hardened BlockState
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

        // Try to get TFC data (safe fallback if not present)
        RockData rockData = null;
        RockLayerSettings rockSettings = null;
        Aquifer aquifer = null;

        if (context.chunkGenerator() instanceof ChunkGeneratorExtension extension) {
            try {
                rockData = ChunkDataProvider.get(context.chunkGenerator()).get(chunk).getRockData();
                rockSettings = extension.rockLayerSettings();
                aquifer = extension.getOrCreateAquifer(chunk);
            } catch (Exception e) {
                // TFC data not available - continue without it
            }
        }

        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                int baseHeight = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, chunkX + x, chunkZ + z);

                boolean prevBlockCanLandslide = false;
                int lastSafeY = baseHeight;
                Block prevBlockHardened = null;

                mutablePos.set(chunkX + x, baseHeight, chunkZ + z);

                for (int y = baseHeight; y >= minY; --y) {
                    mutablePos.setY(y);
                    BlockState stateAt = chunk.getBlockState(mutablePos);

                    LandslideRecipe recipe = stateAt.isAir() ? null : LandslideRecipe.getRecipe(stateAt);
                    boolean isFragile = stateAt.isAir() || TFCFallingBlockEntity.canFallThrough(level, mutablePos, stateAt);

                    // === Landslide / erosion logic ===
                    if (prevBlockCanLandslide) {
                        if (recipe == null) {
                            if (isFragile) {
                                // Place hardened block above fragile area
                                BlockState toPlace = getHardenedState(stateAt, replacementMap, rockData, rockSettings, mutablePos);
                                if (toPlace != null) {
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

                    // === Track previous hardened block ===
                    if (isFragile) {
                        if (prevBlockHardened != null) {
                            mutablePos.setY(y + 1);
                            setBlock(level, chunk, mutablePos, prevBlockHardened.defaultBlockState());
                        }
                        prevBlockHardened = null;
                    } else {
                        prevBlockHardened = getHardenedBlock(stateAt, replacementMap, rockSettings);
                    }
                }
            }
        }

        return true;
    }

    /** Helper: Get hardened state, preferring config replacements over TFC defaults */
    private Block getHardenedBlock(BlockState state, Map<Block, BlockState> replacementMap, RockLayerSettings rockSettings) {
        Block rawBlock = state.getBlock();

        // 1. Check custom replacements first
        BlockState custom = replacementMap.get(rawBlock);
        if (custom != null) {
            return custom.getBlock();
        }

        // 2. Fallback to TFC hardened (if available)
        if (rockSettings != null) {
            return rockSettings.getHardened(rawBlock);
        }

        return rawBlock; // fallback to itself
    }

    /** Helper: Get hardened state for placement */
    private BlockState getHardenedState(BlockState state, Map<Block, BlockState> replacementMap,
                                        RockData rockData, RockLayerSettings rockSettings, BlockPos pos) {
        Block raw = state.getBlock();
        BlockState custom = replacementMap.get(raw);

        if (custom != null) {
            return custom;
        }

        if (rockData != null && rockSettings != null) {
            // Try to get proper hardened version from TFC rock data
            try {
                return rockData.getRock(pos.getX(), pos.getY(), pos.getZ()).hardened().defaultBlockState();
            } catch (Exception ignored) {}
        }

        return null;
    }

    private void setBlock(WorldGenLevel level, ChunkAccess chunk, BlockPos pos, BlockState state) {
        BlockState prev = chunk.setBlockState(pos, state, false);
        if (prev != null && prev.hasBlockEntity()) {
            chunk.removeBlockEntity(pos);
        }
        if (state.hasPostProcess(level, pos)) {
            chunk.markPosForPostprocessing(pos);
        }
    }
}
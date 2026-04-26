package com.bumppo109.firma_compat.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class ErosionFeatureConfig implements FeatureConfiguration {

    public static final Codec<ErosionFeatureConfig> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    RawToHardenedPair.CODEC
                            .listOf()
                            .fieldOf("replacements")
                            .forGetter(ErosionFeatureConfig::getReplacements)
            ).apply(instance, ErosionFeatureConfig::new)
    );

    private final List<RawToHardenedPair> replacements;

    public ErosionFeatureConfig(List<RawToHardenedPair> replacements) {
        this.replacements = replacements != null ? List.copyOf(replacements) : List.of();
    }

    public List<RawToHardenedPair> getReplacements() {
        return replacements;
    }

    // ====================== INNER RECORD ======================
    public record RawToHardenedPair(ResourceLocation raw, ResourceLocation hardened) {

        public static final Codec<RawToHardenedPair> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        ResourceLocation.CODEC.fieldOf("raw").forGetter(RawToHardenedPair::raw),
                        ResourceLocation.CODEC.fieldOf("hardened").forGetter(RawToHardenedPair::hardened)
                ).apply(instance, RawToHardenedPair::new)
        );

        public Block getRawBlock() {
            return ForgeRegistries.BLOCKS.getValue(raw);
        }

        public Block getHardenedBlock() {
            return ForgeRegistries.BLOCKS.getValue(hardened);
        }

        public BlockState getHardenedState() {
            Block block = getHardenedBlock();
            return block != null ? block.defaultBlockState() : null;
        }
    }
}
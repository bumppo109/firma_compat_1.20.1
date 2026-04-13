package com.bumppo109.firma_compat.datagen.assets;

import com.bumppo109.firma_compat.block.CompatOre;
import net.mehvahdjukaar.stone_zone.StoneZone;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.fml.ModList;

import java.util.Locale;

public class StoneZoneLang extends LanguageProvider {
    public StoneZoneLang(PackOutput output) {
        super(output, StoneZone.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add("block_type.firma_compat.loose", "%s Loose");
        add("block_type.firma_compat.hardened", "%s Hardened");
        add("block_type.firma_compat.aqueduct", "%s Aqueduct");
        add("item_type.firma_compat.brick", "%s Brick");

        add("block_type.firma_compat.loose_cobble", "Loose %s Cobble");
        add("block_type.firma_compat.hardened_cobble", "Hardened %s Cobble");

        add("block_type.firma_compat.flagstones", "%s Flagstones");
        add("block_type.firma_compat.flagstone_stairs", "%s Flagstone Stairs");
        add("block_type.firma_compat.flagstone_slab", "%s Flagstone Slab");
        add("block_type.firma_compat.cobbled_road", "%s Cobbled Road");
        add("block_type.firma_compat.cobbled_road_stairs", "%s Cobbled Road Stairs");
        add("block_type.firma_compat.cobbled_road_slab", "%s Cobbled Road Slab");
        add("block_type.firma_compat.sett_road", "%s Sett Road");
        add("block_type.firma_compat.sett_road_stairs", "%s Sett Road Stairs");
        add("block_type.firma_compat.sett_road_slab", "%s Sett Road Slab");
        add("item_type.firma_compat.flagstone", "%s Flagstone");

        add("item_type.firma_compat.shingle", "%s Shingle");
        add("item_type.firma_compat.shingles", "%s Shingle");
        add("item_type.firma_compat.shingle_stair", "%s Shingle Stairs");
        add("item_type.firma_compat.shingle_slab", "%s Shingle Slab");

        //TODO - ore names not going through correctly
        for(CompatOre ore :CompatOre.values()) {
            if (ore.isGraded()) {
                for (CompatOre.Grade grade : CompatOre.Grade.values()) {
                    add("block_type.firma_compat." + grade.name().toLowerCase(Locale.ROOT) + "_" + ore.name().toLowerCase(Locale.ROOT) + "_ore", capitalizeParts(grade.name()) + " %s " + capitalizeParts(ore.name()) + " Ore");
                }
            } else {
                add("block_type.firma_compat." + ore.name().toLowerCase(Locale.ROOT) + "_ore", "%s " + capitalizeParts(ore.name()) + " Ore");
            }
        }

        if(ModList.get().isLoaded("firmalife")){
            add("block_type.firma_compat.poor_chromite_ore", "Poor %s Chromite Ore");
            add("block_type.firma_compat.normal_chromite_ore", "Normal %s Chromite Ore");
            add("block_type.firma_compat.rich_chromite_ore", "Rich %s Chromite Ore");
        }
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    private String capitalizeParts(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        // If no underscore → treat as single word and just capitalize first letter
        if (!input.contains("_")) {
            return Character.toUpperCase(input.charAt(0)) +
                    input.substring(1).toLowerCase(Locale.ROOT);
        }

        // Has underscores → split and capitalize each part
        String[] parts = input.split("_");
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (part.isEmpty()) {
                continue;
            }

            result.append(Character.toUpperCase(part.charAt(0)))
                    .append(part.substring(1).toLowerCase(Locale.ROOT));

            if (i < parts.length - 1) {
                result.append(" ");
            }
        }

        return result.toString();
    }
}

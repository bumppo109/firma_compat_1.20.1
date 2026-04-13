package com.bumppo109.firma_compat;

import net.dries007.tfc.util.DataManager;
import net.dries007.tfc.util.Helpers;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

import static net.dries007.tfc.util.Helpers.resourceLocation;

public class FirmaCompatHelpers {
    public static ResourceLocation modIdentifier(String name) {
        return resourceLocation("firma_compat", name);
    }

    public static ModelLayerLocation layerId(String name)
    {
        return layerId(name, "main");
    }

    /**
     * Creates {@link ModelLayerLocation} in the default manner
     */
    public static ModelLayerLocation layerId(String name, String part)
    {
        return new ModelLayerLocation(modIdentifier(name), part);
    }
}

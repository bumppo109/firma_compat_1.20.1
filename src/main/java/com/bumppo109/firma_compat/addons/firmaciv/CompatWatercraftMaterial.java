package com.bumppo109.firma_compat.addons.firmaciv;

import com.alekiponi.alekiships.common.entity.vehicle.AbstractVehicle;
import com.alekiponi.alekiships.util.BoatMaterial;
import com.alekiponi.firmaciv.common.entity.vehicle.CanoeEntity;
import com.bumppo109.firma_compat.block.CompatWood;
import com.bumppo109.firma_compat.item.ModItems;
import com.nebby1999.firmacivplus.FirmaCivPlusEntities;
import com.nebby1999.firmacivplus.WatercraftMaterial;
import com.therighthon.afc.common.items.AFCItems;
import java.util.Optional;
import net.dries007.tfc.common.blocks.wood.Wood.BlockType;
import net.dries007.tfc.util.registry.RegistryWood;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.RegistryObject;

public enum CompatWatercraftMaterial implements WatercraftMaterial {
    // Softwood = true, Hardwood = false
    ACACIA(CompatWood.ACACIA, true),      // Softwood
    BIRCH(CompatWood.BIRCH, true),        // Softwood
    CHERRY(CompatWood.CHERRY, true),      // Softwood
    SPRUCE(CompatWood.SPRUCE, true),      // Softwood
    MANGROVE(CompatWood.MANGROVE, true),  // Softwood

    OAK(CompatWood.OAK, false),           // Hardwood
    DARK_OAK(CompatWood.DARK_OAK, false), // Hardwood
    JUNGLE(CompatWood.JUNGLE, false),     // Hardwood

    // Nether woods (usually treated as hardwoods)
    CRIMSON(CompatWood.CRIMSON, false),
    WARPED(CompatWood.WARPED, false)
    ;
    

    public final boolean isSoftwood;
    public final CompatWood wood;

    private CompatWatercraftMaterial(CompatWood wood, boolean isSoftwood) {
        this.wood = wood;
        this.isSoftwood = isSoftwood;
    }

    public Item getRailing() {
        return (Item)((RegistryObject) ModItems.LUMBER.get(this.wood)).get();
    }

    public Item getStrippedLog() {
        return ((Block)this.wood.strippedLog()).asItem();
    }

    public boolean withstandsLava() {
        return false;
    }

    public BlockState getDeckBlock() {
        return ((Block)this.wood.planks()).defaultBlockState();
    }

    public Optional<EntityType<? extends AbstractVehicle>> getEntityType(BoatMaterial.BoatType boatType) {
        Optional var10000;
        switch (boatType) {
            case ROWBOAT -> var10000 = Optional.of((EntityType)((RegistryObject) FirmaCivPlusEntities.getRowboats().get(this)).get());
            case SLOOP -> var10000 = Optional.of((EntityType)((RegistryObject)FirmaCivPlusEntities.getSloops().get(this)).get());
            case CONSTRUCTION_SLOOP -> var10000 = Optional.of((EntityType)((RegistryObject)FirmaCivPlusEntities.getSloopsUnderConstruction().get(this)).get());
            default -> throw new IncompatibleClassChangeError();
        }

        return var10000;
    }

    public Optional<EntityType<? extends CanoeEntity>> getCanoeType() {
        return Optional.of((EntityType)((RegistryObject)FirmaCivPlusEntities.getCanoes().get(this)).get());
    }

    public String getSerializedName() {
        return this.wood.getSerializedName();
    }

    public RegistryWood getWood() {
        return this.wood;
    }

    public boolean isSoftwood() {
        return this.isSoftwood;
    }
}

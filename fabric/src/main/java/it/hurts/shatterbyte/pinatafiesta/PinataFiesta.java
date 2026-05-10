package it.hurts.shatterbyte.pinatafiesta;

import it.hurts.shatterbyte.pinatafiesta.content.ModContent;
import it.hurts.shatterbyte.pinatafiesta.entity.PinataEntity;
import it.hurts.shatterbyte.pinatafiesta.fabric.FabricRegistrar;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

public class PinataFiesta implements ModInitializer {
    
    @Override
    public void onInitialize() {
        ModContent.register(new FabricRegistrar());
        FabricDefaultAttributeRegistry.register(ModContent.pinataEntity(), PinataEntity.createAttributes());
        CommonClass.init();
    }
}

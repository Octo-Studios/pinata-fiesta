package it.hurts.shatterbyte.pinatafiesta;

import it.hurts.shatterbyte.pinatafiesta.content.ModContent;
import it.hurts.shatterbyte.pinatafiesta.data.PinataTemplateManager;
import it.hurts.shatterbyte.pinatafiesta.entity.PinataEntity;
import it.hurts.shatterbyte.pinatafiesta.fabric.FabricRegistrar;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.server.packs.PackType;

public class PinataFiesta implements ModInitializer {
    @Override
    public void onInitialize() {
        ModContent.register(new FabricRegistrar());
        FabricDefaultAttributeRegistry.register(ModContent.pinataEntity(), PinataEntity.createAttributes());
        ResourceLoader.get(PackType.SERVER_DATA).registerReloadListener(Constants.id("pinata_templates"), PinataTemplateManager.INSTANCE);
        CommonClass.init();
    }
}

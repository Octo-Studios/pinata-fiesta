package it.hurts.shatterbyte.pinatafiesta;


import it.hurts.shatterbyte.pinatafiesta.content.ModContent;
import it.hurts.shatterbyte.pinatafiesta.data.PinataTemplateManager;
import it.hurts.shatterbyte.pinatafiesta.entity.PinataEntity;
import it.hurts.shatterbyte.pinatafiesta.neoforge.NeoForgeRegistrar;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@Mod(Constants.MOD_ID)
public class PinataFiesta {
    public PinataFiesta(IEventBus eventBus) {
        var registrar = new NeoForgeRegistrar();
        ModContent.register(registrar);
        registrar.register(eventBus);
        eventBus.addListener(EntityAttributeCreationEvent.class, this::registerAttributes);
        eventBus.addListener(AddServerReloadListenersEvent.class, event -> {
            event.addListener(Constants.id("pinata_templates"), PinataTemplateManager.INSTANCE);
        });
        CommonClass.init();
    }

    private void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModContent.pinataEntity(), PinataEntity.createAttributes().build());
    }
}

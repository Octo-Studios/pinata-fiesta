package it.hurts.shatterbyte.pinatafiesta;


import it.hurts.shatterbyte.pinatafiesta.command.GivePinataCommand;
import it.hurts.shatterbyte.pinatafiesta.content.ModContent;
import it.hurts.shatterbyte.pinatafiesta.data.PinataTemplateManager;
import it.hurts.shatterbyte.pinatafiesta.entity.PinataEntity;
import it.hurts.shatterbyte.pinatafiesta.neoforge.NeoForgeRegistrar;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@Mod(Constants.MOD_ID)
@EventBusSubscriber(modid = Constants.MOD_ID)
public class PinataFiesta {
    public PinataFiesta(IEventBus eventBus) {
        var registrar = new NeoForgeRegistrar();
        ModContent.register(registrar);
        registrar.register(eventBus);
        eventBus.addListener(EntityAttributeCreationEvent.class, this::registerAttributes);
        CommonClass.init();
    }

    @SubscribeEvent
    public static void onServerReload(AddServerReloadListenersEvent event) {
        event.addListener(Constants.id("pinata_templates"), PinataTemplateManager.INSTANCE);
    }

    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        GivePinataCommand.register(event.getDispatcher());
    }

    private void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModContent.pinataEntity(), PinataEntity.createAttributes().build());
    }
}

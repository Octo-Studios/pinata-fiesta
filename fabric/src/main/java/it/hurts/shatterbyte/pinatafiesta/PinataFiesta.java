package it.hurts.shatterbyte.pinatafiesta;

import it.hurts.shatterbyte.pinatafiesta.command.GivePinataCommand;
import it.hurts.shatterbyte.pinatafiesta.content.ModComponents;
import it.hurts.shatterbyte.pinatafiesta.content.ModContent;
import it.hurts.shatterbyte.pinatafiesta.data.PinataTemplateManager;
import it.hurts.shatterbyte.pinatafiesta.entity.PinataEntity;
import it.hurts.shatterbyte.pinatafiesta.fabric.FabricRegistrar;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.item.v1.ItemComponentTooltipProviderRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.reloader.ResourceReloaderKeys;
import net.fabricmc.fabric.mixin.content.registry.GiveGiftToHeroAccessor;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.packs.PackType;

public class PinataFiesta implements ModInitializer {
    @Override
    public void onInitialize() {
        ModContent.register(new FabricRegistrar());
        FabricDefaultAttributeRegistry.register(ModContent.pinataEntity(), PinataEntity.createAttributes());
        this.registerReloadListeners();
        CommandRegistrationCallback.EVENT.register((dispatcher, buildContext, selection) -> GivePinataCommand.register(dispatcher));
        ItemComponentTooltipProviderRegistry.addAfter(DataComponents.DAMAGE, ModComponents.DROP_DATA_COMPONENT_TYPE);
        CommonClass.init();
    }

    private void registerReloadListeners() {
        ResourceLoader loader = ResourceLoader.get(PackType.SERVER_DATA);
        loader.registerReloadListener(PinataTemplateManager.ID, PinataTemplateManager.INSTANCE);
        loader.addListenerOrdering(ResourceReloaderKeys.Server.RECIPES, PinataTemplateManager.ID);
    }
}

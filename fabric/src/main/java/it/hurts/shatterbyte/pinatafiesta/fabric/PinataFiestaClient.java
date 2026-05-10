package it.hurts.shatterbyte.pinatafiesta.fabric;

import it.hurts.shatterbyte.pinatafiesta.client.entity.PinataModel;
import it.hurts.shatterbyte.pinatafiesta.client.entity.renderer.PinataRenderer;
import it.hurts.shatterbyte.pinatafiesta.content.ModContent;
import it.hurts.shatterbyte.pinatafiesta.content.ModEntityModelLayers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.renderer.entity.PigRenderer;

public class PinataFiestaClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModelLayerRegistry.registerModelLayer(ModEntityModelLayers.PINATA, PinataModel::createBodyLayer);

        EntityRendererRegistry.register(ModContent.pinataEntity(), PinataRenderer::new);
    }
}

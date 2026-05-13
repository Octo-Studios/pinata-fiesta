package it.hurts.shatterbyte.pinatafiesta.fabric;

import it.hurts.shatterbyte.pinatafiesta.client.particle.ConfettiParticle;
import it.hurts.shatterbyte.pinatafiesta.client.entity.PinataModel;
import it.hurts.shatterbyte.pinatafiesta.client.entity.renderer.PinataRenderer;
import it.hurts.shatterbyte.pinatafiesta.client.particle.PaperParticle;
import it.hurts.shatterbyte.pinatafiesta.content.ModContent;
import it.hurts.shatterbyte.pinatafiesta.content.ModEntityModelLayers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.renderer.entity.EntityRenderers;

public class PinataFiestaClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModelLayerRegistry.registerModelLayer(ModEntityModelLayers.PINATA, PinataModel::createBodyLayer);

        EntityRenderers.register(ModContent.pinataEntity(), PinataRenderer::new);
        ParticleProviderRegistry.getInstance().register(ModContent.confettiParticle(), ConfettiParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(ModContent.paperParticle(), PaperParticle.Provider::new);
    }
}

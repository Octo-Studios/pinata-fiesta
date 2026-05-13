package it.hurts.shatterbyte.pinatafiesta.neoforge;

import it.hurts.shatterbyte.pinatafiesta.Constants;
import it.hurts.shatterbyte.pinatafiesta.client.entity.PinataModel;
import it.hurts.shatterbyte.pinatafiesta.client.entity.renderer.PinataRenderer;
import it.hurts.shatterbyte.pinatafiesta.client.particle.ConfettiParticle;
import it.hurts.shatterbyte.pinatafiesta.client.particle.PaperParticle;
import it.hurts.shatterbyte.pinatafiesta.content.ModContent;
import it.hurts.shatterbyte.pinatafiesta.content.ModEntityModelLayers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public final class PinataFiestaNeoForgeClient {
    private PinataFiestaNeoForgeClient() {

    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModContent.pinataEntity(), PinataRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModEntityModelLayers.PINATA, PinataModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModContent.confettiParticle(), ConfettiParticle.Provider::new);
        event.registerSpriteSet(ModContent.paperParticle(), PaperParticle.Provider::new);
    }
}

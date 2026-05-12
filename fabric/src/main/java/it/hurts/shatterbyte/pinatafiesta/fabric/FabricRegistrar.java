package it.hurts.shatterbyte.pinatafiesta.fabric;

import it.hurts.shatterbyte.pinatafiesta.Constants;
import it.hurts.shatterbyte.pinatafiesta.content.ModContent;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class FabricRegistrar implements ModContent.Registrar {
    @Override
    public <T extends EntityType<?>> Supplier<T> registerEntity(String name, Supplier<T> entityType) {
        T registered = Registry.register(BuiltInRegistries.ENTITY_TYPE, Constants.id(name), entityType.get());
        return () -> registered;
    }

    @Override
    public <T extends Item> Supplier<T> registerItem(String name, Supplier<T> item) {
        T registered = Registry.register(BuiltInRegistries.ITEM, Constants.id(name), item.get());
        return () -> registered;
    }

    @Override
    public Supplier<SoundEvent> registerSound(String name) {
        SoundEvent registered = Registry.register(BuiltInRegistries.SOUND_EVENT, Constants.id(name), SoundEvent.createVariableRangeEvent(Constants.id(name)));
        return () -> registered;
    }

    @Override
    public <T extends SimpleParticleType> Supplier<T> registerParticle(String name, Supplier<T> particleType) {
        T registered = Registry.register(BuiltInRegistries.PARTICLE_TYPE, Constants.id(name), particleType.get());
        return () -> registered;
    }
}

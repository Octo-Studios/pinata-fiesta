package it.hurts.shatterbyte.pinatafiesta.fabric;

import it.hurts.shatterbyte.pinatafiesta.Constants;
import it.hurts.shatterbyte.pinatafiesta.content.ModContent;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
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
}

package it.hurts.shatterbyte.pinatafiesta.neoforge;

import it.hurts.shatterbyte.pinatafiesta.Constants;
import it.hurts.shatterbyte.pinatafiesta.content.ModContent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class NeoForgeRegistrar implements ModContent.Registrar {
    private final DeferredRegister<EntityType<?>> entityTypes = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Constants.MOD_ID);
    private final DeferredRegister<Item> items = DeferredRegister.create(BuiltInRegistries.ITEM, Constants.MOD_ID);

    @Override
    public <T extends EntityType<?>> Supplier<T> registerEntity(String name, Supplier<T> entityType) {
        return entityTypes.register(name, entityType);
    }

    @Override
    public <T extends Item> Supplier<T> registerItem(String name, Supplier<T> item) {
        return items.register(name, item);
    }

    public void register(IEventBus eventBus) {
        entityTypes.register(eventBus);
        items.register(eventBus);
    }
}

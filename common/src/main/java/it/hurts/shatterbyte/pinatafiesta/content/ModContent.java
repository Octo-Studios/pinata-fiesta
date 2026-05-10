package it.hurts.shatterbyte.pinatafiesta.content;

import it.hurts.shatterbyte.pinatafiesta.Constants;
import it.hurts.shatterbyte.pinatafiesta.entity.PinataEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;

import java.util.function.Supplier;

public final class ModContent {
    public static final String PINATA_ID = "pinata";
    public static final Identifier PINATA_IDENTIFIER = Constants.id(PINATA_ID);
    public static final ResourceKey<EntityType<?>> PINATA_ENTITY_KEY = ResourceKey.create(Registries.ENTITY_TYPE, PINATA_IDENTIFIER);
    public static final ResourceKey<Item> PINATA_SPAWN_EGG_KEY = ResourceKey.create(Registries.ITEM, Constants.id("pinata_spawn_egg"));

    private static Supplier<EntityType<PinataEntity>> pinataEntity;
    private static Supplier<Item> pinataSpawnEgg;
    private static boolean registered;

    private ModContent() {
    }

    public static void register(Registrar registrar) {
        if (registered) {
            return;
        }

        pinataEntity = registrar.registerEntity(
                PINATA_ID,
                () -> EntityType.Builder.of(PinataEntity::new, MobCategory.MISC)
                        .sized(0.9F, 1.15F)
                        .clientTrackingRange(10)
                        .updateInterval(3)
                        .build(PINATA_ENTITY_KEY)
        );
        pinataSpawnEgg = registrar.registerItem(
                "pinata_spawn_egg",
                () -> new SpawnEggItem(new Item.Properties().stacksTo(16).spawnEgg(pinataEntity()).setId(PINATA_SPAWN_EGG_KEY))
        );
        registered = true;
    }

    public static EntityType<PinataEntity> pinataEntity() {
        return pinataEntity.get();
    }

    public static Item pinataSpawnEgg() {
        return pinataSpawnEgg.get();
    }

    public interface Registrar {
        <T extends EntityType<?>> Supplier<T> registerEntity(String name, Supplier<T> entityType);

        <T extends Item> Supplier<T> registerItem(String name, Supplier<T> item);
    }
}

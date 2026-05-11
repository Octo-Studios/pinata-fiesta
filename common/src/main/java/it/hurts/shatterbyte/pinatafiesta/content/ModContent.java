package it.hurts.shatterbyte.pinatafiesta.content;

import it.hurts.shatterbyte.pinatafiesta.Constants;
import it.hurts.shatterbyte.pinatafiesta.entity.PinataEntity;
import it.hurts.shatterbyte.pinatafiesta.item.PinataSpawnerItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public final class ModContent {
    public static final String PINATA_ID = "pinata";
    public static final Identifier PINATA_IDENTIFIER = Constants.id(PINATA_ID);
    public static final ResourceKey<EntityType<?>> PINATA_ENTITY_KEY = ResourceKey.create(Registries.ENTITY_TYPE, PINATA_IDENTIFIER);
    public static final ResourceKey<Item> PINATA_SPAWNER_KEY = ResourceKey.create(Registries.ITEM, Constants.id("pinata_spawner"));

    private static Supplier<EntityType<PinataEntity>> pinataEntity;
    private static Supplier<Item> pinataSpawner;
    private static Supplier<SoundEvent> pinataSpawnSound;
    private static Supplier<SoundEvent> pinataDeathSound;
    private static Supplier<SoundEvent> pinataHurt1Sound;
    private static Supplier<SoundEvent> pinataHurt2Sound;
    private static Supplier<SoundEvent> pinataHurt3Sound;
    private static Supplier<SoundEvent> pinataHurt4Sound;
    private static Supplier<SoundEvent> pinataHurt5Sound;
    private static Supplier<SoundEvent> pinataHurt6Sound;
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
        pinataSpawner = registrar.registerItem(
                "pinata_spawner",
                () -> new PinataSpawnerItem(new Item.Properties().stacksTo(16).setId(PINATA_SPAWNER_KEY))
        );
        pinataSpawnSound = registrar.registerSound("entity.pinata.spawn");
        pinataDeathSound = registrar.registerSound("entity.pinata.death");
        pinataHurt1Sound = registrar.registerSound("entity.pinata.hurt1");
        pinataHurt2Sound = registrar.registerSound("entity.pinata.hurt2");
        pinataHurt3Sound = registrar.registerSound("entity.pinata.hurt3");
        pinataHurt4Sound = registrar.registerSound("entity.pinata.hurt4");
        pinataHurt5Sound = registrar.registerSound("entity.pinata.hurt5");
        pinataHurt6Sound = registrar.registerSound("entity.pinata.hurt6");

        registered = true;
    }

    public static EntityType<PinataEntity> pinataEntity() {
        return pinataEntity.get();
    }

    public static Item pinataSpawner() {
        return pinataSpawner.get();
    }

    public static SoundEvent pinataSpawnSound() {
        return pinataSpawnSound.get();
    }

    public static SoundEvent pinataDeathSound() {
        return pinataDeathSound.get();
    }

    public static SoundEvent randomPinataHurtSound(int variantIndex) {
        return switch (variantIndex) {
            case 0 -> pinataHurt1Sound.get();
            case 1 -> pinataHurt2Sound.get();
            case 3 -> pinataHurt3Sound.get();
            case 4 -> pinataHurt4Sound.get();
            case 5 -> pinataHurt5Sound.get();
            default -> pinataHurt6Sound.get();
        };
    }

    public interface Registrar {
        <T extends EntityType<?>> Supplier<T> registerEntity(String name, Supplier<T> entityType);

        <T extends Item> Supplier<T> registerItem(String name, Supplier<T> item);

        Supplier<SoundEvent> registerSound(String name);
    }
}

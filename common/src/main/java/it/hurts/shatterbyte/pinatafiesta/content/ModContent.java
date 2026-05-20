package it.hurts.shatterbyte.pinatafiesta.content;

import it.hurts.shatterbyte.pinatafiesta.Constants;
import it.hurts.shatterbyte.pinatafiesta.data.PinataDropData;
import it.hurts.shatterbyte.pinatafiesta.entity.PinataEntity;
import it.hurts.shatterbyte.pinatafiesta.item.PinataSpawnerItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.particles.SimpleParticleType;
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
    private static Supplier<SoundEvent> pinataHurtSound;

    private static Supplier<SimpleParticleType> sunsetConfetti;
    private static Supplier<SimpleParticleType> sunsetPaper;
    private static Supplier<SimpleParticleType> aquamarineConfetti;
    private static Supplier<SimpleParticleType> aquamarinePaper;
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
                () -> new PinataSpawnerItem(new Item.Properties()
                        .component(ModComponents.SKINS_COMPONENT_TYPE, ModPinataSkins.getSkinIds())
                        .component(ModComponents.DROP_DATA_COMPONENT_TYPE, PinataDropData.EMPTY)
                        .stacksTo(16)
                        .setId(PINATA_SPAWNER_KEY)
                )
        );
        pinataSpawnSound = registrar.registerSound("entity.pinata.spawn");
        pinataDeathSound = registrar.registerSound("entity.pinata.death");
        pinataHurtSound = registrar.registerSound("entity.pinata.hurt");

        sunsetConfetti = registrar.registerParticle("sunset_confetti", () -> new SimpleParticleType(false) {});
        sunsetPaper = registrar.registerParticle("sunset_paper", () -> new SimpleParticleType(false) {});
        aquamarineConfetti = registrar.registerParticle("aquamarine_confetti", () -> new SimpleParticleType(false) {});
        aquamarinePaper = registrar.registerParticle("aquamarine_paper", () -> new SimpleParticleType(false) {});

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

    public static SoundEvent pinataHurtSound() {
        return pinataHurtSound.get();
    }

    public static SimpleParticleType sunsetConfetti() {
        return sunsetConfetti.get();
    }

    public static SimpleParticleType sunsetPaper() {
        return sunsetPaper.get();
    }

    public static SimpleParticleType aquamarineConfetti() {
        return aquamarineConfetti.get();
    }

    public static SimpleParticleType aquamarinePaper() {
        return aquamarinePaper.get();
    }

    public interface Registrar {
        <T extends EntityType<?>> Supplier<T> registerEntity(String name, Supplier<T> entityType);

        <T extends Item> Supplier<T> registerItem(String name, Supplier<T> item);

        Supplier<SoundEvent> registerSound(String name);

        <T extends SimpleParticleType> Supplier<T> registerParticle(String name, Supplier<T> particleType);
    }
}

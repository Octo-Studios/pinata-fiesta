package it.hurts.shatterbyte.pinatafiesta.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.hurts.shatterbyte.pinatafiesta.content.ModPinataSkins;
import net.minecraft.resources.Identifier;

import java.util.List;

public record PinataTemplate(
        int hits,
        List<Identifier> skins,
        PinataDropData dropData
) {
    public static final Codec<PinataTemplate> CODEC =
            RecordCodecBuilder.create(instance ->
                    instance.group(
                            Codec.INT.optionalFieldOf("hits", 10)
                                    .forGetter(PinataTemplate::hits),

                            Identifier.CODEC.listOf()
                                    .optionalFieldOf("skins", List.of())
                                    .forGetter(PinataTemplate::skins),

                            PinataDropData.CODEC
                                    .optionalFieldOf("drop_data", PinataDropData.EMPTY)
                                    .forGetter(PinataTemplate::dropData)
                    ).apply(instance, PinataTemplate::new)
            );

    public static final PinataTemplate DEFAULT =
            new PinataTemplate(
                    10,
                    ModPinataSkins.getSkinIds(),
                    PinataDropData.EMPTY
            );
}
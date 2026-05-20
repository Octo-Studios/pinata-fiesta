package it.hurts.shatterbyte.pinatafiesta.data;

import com.mojang.serialization.MapCodec;

public record PinataActionType<T extends PinataAction>(
        MapCodec<T> codec
) {}
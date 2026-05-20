package it.hurts.shatterbyte.pinatafiesta.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import it.hurts.shatterbyte.pinatafiesta.entity.PinataEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public interface PinataAction {
    PinataActionType<?> getType();

    void execute(ServerLevel level, PinataEntity pinata, @Nullable Player player);

    Codec<PinataAction> CODEC = PinataActionTypes.TYPE_CODEC.dispatch(
            "type",
            PinataAction::getType,
            PinataActionType::codec
    );
}
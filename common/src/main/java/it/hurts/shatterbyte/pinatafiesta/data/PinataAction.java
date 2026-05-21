package it.hurts.shatterbyte.pinatafiesta.data;

import com.mojang.serialization.Codec;
import it.hurts.shatterbyte.pinatafiesta.entity.PinataEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface PinataAction {
    PinataActionType<?> getType();

    void execute(ServerLevel level, PinataEntity pinata, @Nullable Player player);

    List<Component> getTooltips();

    Codec<PinataAction> CODEC = PinataActionTypes.TYPE_CODEC.dispatch(
            "type",
            PinataAction::getType,
            PinataActionType::codec
    );

    static MutableComponent bulleted(Component component, ChatFormatting bulletColor) {
        return Component.literal("• ").withStyle(bulletColor).append(component);
    }

    static MutableComponent bulleted(Component component) {
        return PinataAction.bulleted(component, ChatFormatting.WHITE);
    }
}
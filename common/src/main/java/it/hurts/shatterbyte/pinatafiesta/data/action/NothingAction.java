package it.hurts.shatterbyte.pinatafiesta.data.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import it.hurts.shatterbyte.pinatafiesta.data.PinataAction;
import it.hurts.shatterbyte.pinatafiesta.data.PinataActionType;
import it.hurts.shatterbyte.pinatafiesta.data.PinataActionTypes;
import it.hurts.shatterbyte.pinatafiesta.entity.PinataEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record NothingAction() implements PinataAction {
    public static final MapCodec<NothingAction> CODEC = MapCodec.unit(new NothingAction());

    @Override
    public PinataActionType<?> getType() {
        return PinataActionTypes.NOTHING;
    }

    @Override
    public List<Component> getTooltips() {
        return List.of(Component.translatable(
                "item.pinatafiesta.pinata_spawner.tooltip.nothing"
        ).withStyle(ChatFormatting.DARK_GRAY));
    }

    @Override
    public void execute(ServerLevel level, PinataEntity pinata, @Nullable Player player) {}
}
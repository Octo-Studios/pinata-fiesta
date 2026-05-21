package it.hurts.shatterbyte.pinatafiesta.data.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.hurts.shatterbyte.pinatafiesta.data.PinataAction;
import it.hurts.shatterbyte.pinatafiesta.data.PinataActionType;
import it.hurts.shatterbyte.pinatafiesta.data.PinataActionTypes;
import it.hurts.shatterbyte.pinatafiesta.entity.PinataEntity;
import it.hurts.shatterbyte.pinatafiesta.util.TooltipRenderUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public record MultipleAction(List<PinataAction> actions) implements PinataAction {
    public static final MapCodec<MultipleAction> CODEC =
            RecordCodecBuilder.mapCodec(instance ->
                    instance.group(
                            Codec.lazyInitialized(() -> PinataAction.CODEC)
                                    .listOf()
                                    .fieldOf("actions")
                                    .forGetter(MultipleAction::actions)
                    ).apply(instance, MultipleAction::new)
            );

    @Override
    public PinataActionType<?> getType() {
        return PinataActionTypes.MULTIPLE;
    }

    @Override
    public void execute(
            ServerLevel level,
            PinataEntity pinata,
            @Nullable Player player
    ) {
        for (PinataAction action : actions) {
            action.execute(level, pinata, player);
        }
    }

    @Override
    public List<Component> getTooltips() {
        List<Component> tooltips = new ArrayList<>();

        tooltips.add(
                Component.translatable(
                        "item.pinatafiesta.pinata_spawner.tooltip.multiple"
                ).withStyle(ChatFormatting.AQUA)
        );

        actions.forEach(action ->
                TooltipRenderUtil.appendNestedTooltips(
                        tooltips,
                        action.getTooltips()
                )
        );

        return tooltips;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final List<PinataAction> actions = new ArrayList<>();

        public Builder add(PinataAction action) {
            this.actions.add(action);
            return this;
        }

        public MultipleAction build() {
            return new MultipleAction(List.copyOf(actions));
        }
    }
}
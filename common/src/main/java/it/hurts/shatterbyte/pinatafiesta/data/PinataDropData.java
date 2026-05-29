package it.hurts.shatterbyte.pinatafiesta.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.hurts.shatterbyte.pinatafiesta.data.action.WeightedAction;
import it.hurts.shatterbyte.pinatafiesta.entity.PinataEntity;
import it.hurts.shatterbyte.pinatafiesta.util.TooltipRenderUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public record PinataDropData(
        List<PinataAction> spawnActions,
        List<PinataAction> hitActions,
        List<PinataAction> breakActions
) implements TooltipProvider {
    public static final Codec<PinataDropData> CODEC =
            RecordCodecBuilder.create(instance ->
                    instance.group(
                            PinataAction.CODEC.listOf()
                                    .optionalFieldOf("spawn_actions", List.of())
                                    .forGetter(PinataDropData::spawnActions),

                            PinataAction.CODEC.listOf()
                                    .optionalFieldOf("hit_actions", List.of())
                                    .forGetter(PinataDropData::hitActions),

                            PinataAction.CODEC.listOf()
                                    .optionalFieldOf("break_actions", List.of())
                                    .forGetter(PinataDropData::breakActions)
                    ).apply(instance, PinataDropData::new)
            );

    public static final PinataDropData EMPTY =
            new PinataDropData(List.of(), List.of(), List.of());

    public static Builder builder() {
        return new Builder();
    }

    public void executeSpawnActions(ServerLevel level, PinataEntity pinata, @Nullable Player player) {
        executeActions(spawnActions, level, pinata, player);
    }

    public void executeHitActions(ServerLevel level, PinataEntity pinata, @Nullable Player player) {
        executeActions(hitActions, level, pinata, player);
    }

    public void executeBreakActions(ServerLevel level, PinataEntity pinata, @Nullable Player player) {
        executeActions(breakActions, level, pinata, player);
    }

    private static void executeActions(
            List<PinataAction> actions,
            ServerLevel level,
            PinataEntity pinata,
            @Nullable Player player
    ) {
        for (PinataAction action : actions) {
            action.execute(level, pinata, player);
        }
    }

    @Override
    public void addToTooltip(Item.TooltipContext tooltipContext, Consumer<Component> consumer, TooltipFlag tooltipFlag, DataComponentGetter dataComponentGetter) {
        if (!Minecraft.getInstance().hasShiftDown()) {
            consumer.accept(Component.translatable("item.pinatafiesta.pinata_spawner.tooltip.hold_shift").withStyle(ChatFormatting.GRAY));
            return;
        }

        PinataDropData.appendActionSection(
                consumer,
                "Spawn Actions",
                spawnActions
        );

        PinataDropData.appendActionSection(
                consumer,
                "Hit Actions",
                hitActions
        );

        PinataDropData.appendActionSection(
                consumer,
                "Break Actions",
                breakActions
        );
    }

    private static void appendActionSection(
            Consumer<Component> consumer,
            String translationKey,
            List<PinataAction> actions
    ) {
        if (actions.isEmpty()) {
            return;
        }

        consumer.accept(Component.empty());

        consumer.accept(
                Component.translatable(translationKey)
        );

        for (PinataAction action : actions) {
            TooltipRenderUtil.appendNestedTooltips(
                    consumer,
                    action.getTooltips()
            );
        }
    }

    public static class Builder {
        private final List<PinataAction> spawnActions = new ArrayList<>();
        private final List<PinataAction> hitActions = new ArrayList<>();
        private final List<PinataAction> breakActions = new ArrayList<>();

        public Builder addSpawnAction(PinataAction action) {
            spawnActions.add(action);
            return this;
        }

        public Builder addHitAction(PinataAction action) {
            hitActions.add(action);
            return this;
        }

        public Builder addBreakAction(PinataAction action) {
            breakActions.add(action);
            return this;
        }

        public Builder addSpawnActions(Collection<? extends PinataAction> actions) {
            spawnActions.addAll(actions);
            return this;
        }

        public Builder addHitActions(Collection<? extends PinataAction> actions) {
            hitActions.addAll(actions);
            return this;
        }

        public Builder addBreakActions(Collection<? extends PinataAction> actions) {
            breakActions.addAll(actions);
            return this;
        }

        public PinataDropData build() {
            return new PinataDropData(
                    List.copyOf(spawnActions),
                    List.copyOf(hitActions),
                    List.copyOf(breakActions)
            );
        }
    }
}
package it.hurts.shatterbyte.pinatafiesta.data.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.hurts.shatterbyte.pinatafiesta.data.PinataAction;
import it.hurts.shatterbyte.pinatafiesta.data.PinataActionType;
import it.hurts.shatterbyte.pinatafiesta.data.PinataActionTypes;
import it.hurts.shatterbyte.pinatafiesta.entity.PinataEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public record WeightedAction(List<Entry> entries) implements PinataAction {
    public static final MapCodec<WeightedAction> CODEC =
            Entry.CODEC.listOf()
                    .fieldOf("entries")
                    .xmap(WeightedAction::new, WeightedAction::entries);

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public PinataActionType<?> getType() {
        return PinataActionTypes.WEIGHTED;
    }

    @Override
    public Component getTooltip() {
        return Component.translatable("item.pinatafiesta.pinata_spawner.tooltip.weighted_rewards").withColor(0xfff4ae89);
    }

    public List<Component> getEntryTooltips() {
        List<Component> tooltips = new ArrayList<>();
        int totalWeight = this.getTotalWeight();

        if (totalWeight <= 0) {
            return tooltips;
        }

        for (Entry entry : entries) {
            double percentage = (entry.weight * 100d) / totalWeight;

            tooltips.add(
                    Component.literal("• ").withStyle(ChatFormatting.GRAY).append(
                            Component.translatable(
                                    "item.pinatafiesta.pinata_spawner.tooltip.weighted_entry",
                                    entry.action.getTooltip(),
                                    String.format(Locale.ROOT, "%.1f%%", percentage)
                            ).withStyle(ChatFormatting.DARK_GRAY)
                    )
            );
        }

        return tooltips;
    }

    public int getTotalWeight() {
        int totalWeight = 0;
        for (Entry entry : entries) {
            totalWeight += entry.weight;
        }
        return totalWeight;
    }

    @Override
    public void execute(ServerLevel level, PinataEntity pinata, @Nullable Player player) {
        int totalWeight = this.getTotalWeight();
        if (totalWeight <= 0) {
            return;
        }

        int value = level.getRandom().nextInt(totalWeight);

        int accumulated = 0;

        for (Entry entry : entries) {
            accumulated += entry.weight();

            if (value < accumulated) {
                entry.action().execute(level, pinata, player);
                return;
            }
        }
    }

    public record Entry(int weight, PinataAction action) {
        public static final Codec<Entry> CODEC =
                RecordCodecBuilder.create(instance ->
                        instance.group(
                                Codec.INT.fieldOf("weight")
                                        .forGetter(Entry::weight),

                                Codec.lazyInitialized(() -> PinataAction.CODEC)
                                        .fieldOf("action")
                                        .forGetter(Entry::action)
                        ).apply(instance, Entry::new)
                );
    }

    public static class Builder {
        private final List<Entry> entries = new ArrayList<>();

        public Builder add(int weight, PinataAction action) {
            entries.add(new Entry(weight, action));
            return this;
        }

        public Builder add(Entry entry) {
            entries.add(entry);
            return this;
        }

        public Builder addAll(Collection<Entry> entries) {
            this.entries.addAll(entries);
            return this;
        }

        public WeightedAction build() {
            return new WeightedAction(List.copyOf(entries));
        }
    }
}
package it.hurts.shatterbyte.pinatafiesta.data.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.hurts.shatterbyte.pinatafiesta.data.PinataAction;
import it.hurts.shatterbyte.pinatafiesta.data.PinataActionType;
import it.hurts.shatterbyte.pinatafiesta.data.PinataActionTypes;
import it.hurts.shatterbyte.pinatafiesta.entity.PinataEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record WeightedAction(List<Entry> entries) implements PinataAction {
    public static final MapCodec<WeightedAction> CODEC =
            Entry.CODEC.listOf()
                    .fieldOf("entries")
                    .xmap(WeightedAction::new, WeightedAction::entries);

    @Override
    public PinataActionType<?> getType() {
        return PinataActionTypes.WEIGHTED;
    }

    @Override
    public void execute(ServerLevel level, PinataEntity pinata, @Nullable Player player) {
        int totalWeight = 0;

        for (Entry entry : entries) {
            totalWeight += entry.weight();
        }

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
                                Codec.INT.fieldOf("weight").forGetter(Entry::weight),
                                PinataAction.CODEC.fieldOf("action").forGetter(Entry::action)
                        ).apply(instance, Entry::new)
                );
    }
}
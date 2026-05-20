package it.hurts.shatterbyte.pinatafiesta.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.hurts.shatterbyte.pinatafiesta.entity.PinataEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public record PinataDropData(
        List<PinataAction> spawnActions,
        List<PinataAction> hitActions,
        List<PinataAction> breakActions
) {
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

    public PinataDropData() {
        this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    public PinataDropData addSpawnAction(PinataAction action) {
        this.spawnActions.add(action);
        return this;
    }

    public PinataDropData addHitAction(PinataAction action) {
        this.hitActions.add(action);
        return this;
    }

    public PinataDropData addBreakAction(PinataAction action) {
        this.breakActions.add(action);
        return this;
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
}
package it.hurts.shatterbyte.pinatafiesta.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import it.hurts.shatterbyte.pinatafiesta.Constants;
import it.hurts.shatterbyte.pinatafiesta.data.action.*;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;

public class PinataActionTypes {
    private static final Map<Identifier, PinataActionType<?>> TYPES = new HashMap<>();
    private static final Map<PinataActionType<?>, Identifier> IDS = new HashMap<>();

    public static final PinataActionType<DropItemAction> DROP_ITEM =
            register(
                    Constants.id("drop_item"),
                    new PinataActionType<>(DropItemAction.CODEC)
            );

    public static final PinataActionType<RunCommandAction> RUN_COMMAND =
            register(
                    Constants.id("run_command"),
                    new PinataActionType<>(RunCommandAction.CODEC)
            );

    public static final PinataActionType<WeightedAction> WEIGHTED =
            register(
                    Constants.id("weighted"),
                    new PinataActionType<>(WeightedAction.CODEC)
            );

    public static final PinataActionType<DropExperienceAction> DROP_EXPERIENCE =
            register(
                    Constants.id("drop_experience"),
                    new PinataActionType<>(DropExperienceAction.CODEC)
            );

    public static final PinataActionType<NothingAction> NOTHING =
            register(
                    Constants.id("nothing"),
                    new PinataActionType<>(NothingAction.CODEC)
            );

    public static final Codec<PinataActionType<?>> TYPE_CODEC =
            Identifier.CODEC.flatXmap(
                    id -> {
                        PinataActionType<?> type = TYPES.get(id);

                        if (type == null) {
                            return DataResult.error(() -> "Unknown action type: " + id);
                        }

                        return DataResult.success(type);
                    },
                    type -> {
                        Identifier id = IDS.get(type);

                        if (id == null) {
                            return DataResult.error(() -> "Unregistered action type");
                        }

                        return DataResult.success(id);
                    }
            );

    private static <T extends PinataAction> PinataActionType<T> register(
            Identifier id,
            PinataActionType<T> type
    ) {
        TYPES.put(id, type);
        IDS.put(type, id);
        return type;
    }
}
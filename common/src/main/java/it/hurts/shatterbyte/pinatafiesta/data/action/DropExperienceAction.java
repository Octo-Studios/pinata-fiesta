package it.hurts.shatterbyte.pinatafiesta.data.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.hurts.shatterbyte.pinatafiesta.data.PinataAction;
import it.hurts.shatterbyte.pinatafiesta.data.PinataActionType;
import it.hurts.shatterbyte.pinatafiesta.data.PinataActionTypes;
import it.hurts.shatterbyte.pinatafiesta.entity.PinataEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public record DropExperienceAction(Optional<Integer> min, Optional<Integer> max) implements PinataAction {
    public static final MapCodec<DropExperienceAction> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.INT.optionalFieldOf("min").forGetter(DropExperienceAction::min),
                    Codec.INT.optionalFieldOf("max").forGetter(DropExperienceAction::max)
            ).apply(instance, DropExperienceAction::new)
    );

    @Override
    public PinataActionType<?> getType() {
        return PinataActionTypes.DROP_EXPERIENCE;
    }

    @Override
    public List<Component> getTooltips() {
        return List.of(Component.translatable(
                "item.pinatafiesta.pinata_spawner.tooltip.drop_experience",
                getCountDisplay()
        ).withColor(0xff8af489));
    }

    @Override
    public void execute(ServerLevel level, PinataEntity pinata, @Nullable Player player) {
        ExperienceOrb.award(level, pinata.position().add(0, pinata.getBbHeight() * 0.6f, 0), resolveCount(pinata));
    }

    private int resolveCount(PinataEntity pinata) {
        if (max.isPresent()) {
            int maxCount = max.get();
            int minCount = min.orElse(1);
            if (maxCount > minCount) {
                return minCount + pinata.getRandom().nextInt(maxCount - minCount + 1);
            }
            return minCount;
        }
        return min.orElse(0);
    }

    private String getCountDisplay() {
        if (max.isPresent()) {
            int maxCount = max.get();
            int minCount = min.orElse(1);
            if (maxCount > minCount) {
                return minCount + "-" + maxCount;
            }
            return String.valueOf(minCount);
        }
        return String.valueOf(min.orElse(0));
    }
}

package it.hurts.shatterbyte.pinatafiesta.data.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import it.hurts.shatterbyte.pinatafiesta.data.PinataAction;
import it.hurts.shatterbyte.pinatafiesta.data.PinataActionType;
import it.hurts.shatterbyte.pinatafiesta.data.PinataActionTypes;
import it.hurts.shatterbyte.pinatafiesta.entity.PinataEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStackTemplate;
import org.jetbrains.annotations.Nullable;

public record DropExperienceAction(int points) implements PinataAction {
    public static final MapCodec<DropExperienceAction> CODEC = Codec.INT.fieldOf("points")
            .xmap(DropExperienceAction::new, DropExperienceAction::points);

    @Override
    public PinataActionType<?> getType() {
        return PinataActionTypes.DROP_EXPERIENCE;
    }

    @Override
    public Component getTooltip() {
        return Component.translatable(
                "item.pinatafiesta.pinata_spawner.tooltip.drop_experience",
                points
        ).withColor(0xff8af489);
    }

    @Override
    public void execute(ServerLevel level, PinataEntity pinata, @Nullable Player player) {
        if (player != null) {
            player.giveExperiencePoints(points);
        }
    }
}
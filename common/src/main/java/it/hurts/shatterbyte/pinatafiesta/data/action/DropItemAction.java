package it.hurts.shatterbyte.pinatafiesta.data.action;

import com.mojang.serialization.MapCodec;
import it.hurts.shatterbyte.pinatafiesta.data.PinataAction;
import it.hurts.shatterbyte.pinatafiesta.data.PinataActionType;
import it.hurts.shatterbyte.pinatafiesta.data.PinataActionTypes;
import it.hurts.shatterbyte.pinatafiesta.entity.PinataEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public record DropItemAction(ItemStack stack) implements PinataAction {
    public static final MapCodec<DropItemAction> CODEC = ItemStack.CODEC.fieldOf("stack")
            .xmap(DropItemAction::new, DropItemAction::stack);

    @Override
    public PinataActionType<?> getType() {
        return PinataActionTypes.DROP_ITEM;
    }

    @Override
    public void execute(ServerLevel level, PinataEntity pinata, @Nullable Player player) {
        pinata.spawnAtLocation(level, stack.copy());
    }
}
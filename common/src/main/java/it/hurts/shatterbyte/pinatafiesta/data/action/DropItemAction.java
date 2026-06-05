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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public record DropItemAction(ItemStackTemplate item, Optional<Integer> min, Optional<Integer> max) implements PinataAction {
    public static final MapCodec<DropItemAction> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ItemStackTemplate.CODEC.fieldOf("item").forGetter(DropItemAction::item),
                    Codec.INT.optionalFieldOf("min").forGetter(DropItemAction::min),
                    Codec.INT.optionalFieldOf("max").forGetter(DropItemAction::max)
            ).apply(instance, DropItemAction::new)
    );

    public DropItemAction(ItemStackTemplate item) {
        this(item, Optional.empty(), Optional.empty());
    }

    @Override
    public PinataActionType<?> getType() {
        return PinataActionTypes.DROP_ITEM;
    }

    @Override
    public List<Component> getTooltips() {
        return List.of(Component.translatable(
                "item.pinatafiesta.pinata_spawner.tooltip.drop_item",
                getCountDisplay(),
                item.create().getStyledHoverName()
        ).withColor(0xfff4e489));
    }

    @Override
    public void execute(ServerLevel level, PinataEntity pinata, @Nullable Player player) {
        int count = resolveCount(pinata);
        ItemStack stack;
        if (count != item.count()) {
            stack = item.withCount(count).create();
        } else {
            stack = item.create();
        }
        pinata.spawnAtLocation(level, stack, new Vec3(0, pinata.getBbHeight() * 0.6f, 0));
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

        return min.orElseGet(item::count);
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

        return String.valueOf(min.orElseGet(item::count));
    }
}

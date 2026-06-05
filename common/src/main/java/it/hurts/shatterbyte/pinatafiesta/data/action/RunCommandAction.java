package it.hurts.shatterbyte.pinatafiesta.data.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.hurts.shatterbyte.pinatafiesta.data.PinataAction;
import it.hurts.shatterbyte.pinatafiesta.data.PinataActionType;
import it.hurts.shatterbyte.pinatafiesta.data.PinataActionTypes;
import it.hurts.shatterbyte.pinatafiesta.entity.PinataEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.permissions.PermissionSet;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Optional;

public record RunCommandAction(String command, ExecuteAs execute_as) implements PinataAction {
    public static final MapCodec<RunCommandAction> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.STRING.fieldOf("command").forGetter(RunCommandAction::command),
                    ExecuteAs.CODEC.optionalFieldOf("execute_as", ExecuteAs.PLAYER).forGetter(RunCommandAction::execute_as)
            ).apply(instance, RunCommandAction::new)
    );

    @Override
    public PinataActionType<?> getType() {
        return PinataActionTypes.RUN_COMMAND;
    }

    @Override
    public List<Component> getTooltips() {
        return List.of(Component.translatable(
                "item.pinatafiesta.pinata_spawner.tooltip.run_command",
                Component.literal(command.startsWith("/") ? command : "/"+command).withColor(0xfff48999).withStyle(ChatFormatting.ITALIC)
        ).withColor(0xfff4e489));
    }

    @Override
    public void execute(ServerLevel level, PinataEntity pinata, @Nullable Player player) {
        CommandSourceStack source = level.getServer()
                .createCommandSourceStack()
                .withPermission(PermissionSet.ALL_PERMISSIONS);

        Entity entity = switch (execute_as) {
            case PLAYER -> player;
            case PINATA -> pinata;
            case SERVER -> null;
        };

        if (entity != null) {
            source = source.withEntity(entity).withPosition(entity.position()).withSuppressedOutput();
        }

        level.getServer()
                .getCommands()
                .performPrefixedCommand(source, command);
    }

    enum ExecuteAs implements StringRepresentable {
        PLAYER,
        PINATA,
        SERVER;

        public static final Codec<ExecuteAs> CODEC = StringRepresentable.fromValues(ExecuteAs::values);

        @Override
        public @NonNull String getSerializedName() {
            return this.name().toLowerCase();
        }
    }
}
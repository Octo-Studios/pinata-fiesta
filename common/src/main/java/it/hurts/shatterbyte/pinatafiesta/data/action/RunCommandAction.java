package it.hurts.shatterbyte.pinatafiesta.data.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import it.hurts.shatterbyte.pinatafiesta.data.PinataAction;
import it.hurts.shatterbyte.pinatafiesta.data.PinataActionType;
import it.hurts.shatterbyte.pinatafiesta.data.PinataActionTypes;
import it.hurts.shatterbyte.pinatafiesta.entity.PinataEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.permissions.PermissionSet;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record RunCommandAction(String command) implements PinataAction {
    public static final MapCodec<RunCommandAction> CODEC =
            Codec.STRING.fieldOf("command")
                    .xmap(RunCommandAction::new, RunCommandAction::command);

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

        if (player != null) {
            source = source.withEntity(player).withPosition(player.position()).withSuppressedOutput();
        }

        level.getServer()
                .getCommands()
                .performPrefixedCommand(source, command);
    }
}
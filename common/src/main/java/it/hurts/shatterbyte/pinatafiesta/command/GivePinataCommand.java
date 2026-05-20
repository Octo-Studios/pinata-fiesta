package it.hurts.shatterbyte.pinatafiesta.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import it.hurts.shatterbyte.pinatafiesta.content.ModComponents;
import it.hurts.shatterbyte.pinatafiesta.content.ModContent;
import it.hurts.shatterbyte.pinatafiesta.data.PinataTemplate;
import it.hurts.shatterbyte.pinatafiesta.data.PinataTemplateManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.IdentifierArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.item.ItemStack;

public class GivePinataCommand {
    private static final DynamicCommandExceptionType ERROR_UNKNOWN_TEMPLATE =
            new DynamicCommandExceptionType(id ->
                    Component.literal("Unknown pinata template: " + id)
            );

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("givepinata")
                        .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER))
                        .then(Commands.argument("template", IdentifierArgument.id())
                                        .suggests((context, builder) -> {
                                            for (Identifier id : PinataTemplateManager.INSTANCE.getIds()) {
                                                builder.suggest(id.toString());
                                            }

                                            return builder.buildFuture();
                                        })
                                        .executes(context -> givePinata(
                                                context.getSource(),
                                                IdentifierArgument.getId(context, "template")
                                        ))
                        )
        );
    }

    private static int givePinata(CommandSourceStack source, Identifier templateId)
            throws CommandSyntaxException {

        if (!PinataTemplateManager.INSTANCE.contains(templateId)) {
            throw ERROR_UNKNOWN_TEMPLATE.create(templateId.toString());
        }

        ServerPlayer player = source.getPlayerOrException();

        PinataTemplate template = PinataTemplateManager.INSTANCE.get(templateId);

        ItemStack stack = ModContent.pinataSpawner().getDefaultInstance();

        stack.set(ModComponents.PINATA_HITS_COMPONENT_TYPE, template.pinataHits());
        stack.set(ModComponents.SKINS_COMPONENT_TYPE, template.skins());
        stack.set(ModComponents.DROP_DATA_COMPONENT_TYPE, template.dropData());
        player.getInventory().add(stack);
        source.sendSuccess(
                () -> Component.literal(
                        "Given pinata template: " + templateId
                ),
                false
        );

        return 1;
    }
}
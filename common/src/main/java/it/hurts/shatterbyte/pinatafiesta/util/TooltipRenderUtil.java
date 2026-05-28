package it.hurts.shatterbyte.pinatafiesta.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class TooltipRenderUtil {
    private TooltipRenderUtil() {

    }

    public static void appendNestedTooltips(List<Component> output, List<? extends Component> nested) {
        for (int i = 0; i < nested.size(); i++) {
            Component tooltip = nested.get(i);

            if (i == 0) {
                output.add(
                        Component.literal("• ")
                                .withStyle(ChatFormatting.GRAY)
                                .append(tooltip)
                );
            } else if (i == nested.size() - 1) {
                output.add(
                        Component.literal("┗ ")
                                .withStyle(ChatFormatting.DARK_GRAY)
                                .append(tooltip)
                );
            } else {
                if (!tooltip.getString().startsWith("•")) {
                    output.add(
                            Component.literal("| ")
                                    .withStyle(ChatFormatting.DARK_GRAY)
                                    .append(tooltip)
                    );

                    continue;
                }

                output.add(
                        Component.literal("┠ ")
                                .withStyle(ChatFormatting.DARK_GRAY)
                                .append(tooltip)
                );
            }
        }
    }

    public static void appendNestedTooltips(
            Consumer<Component> consumer,
            List<? extends Component> nested
    ) {
        List<Component> rendered = new ArrayList<>();

        appendNestedTooltips(rendered, nested);

        rendered.forEach(consumer);
    }
}
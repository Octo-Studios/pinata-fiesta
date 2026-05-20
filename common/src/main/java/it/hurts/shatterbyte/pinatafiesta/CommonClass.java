package it.hurts.shatterbyte.pinatafiesta;

import it.hurts.shatterbyte.pinatafiesta.content.ModComponents;
import it.hurts.shatterbyte.pinatafiesta.content.ModContent;
import it.hurts.shatterbyte.pinatafiesta.data.PinataDropData;
import it.hurts.shatterbyte.pinatafiesta.data.action.DropItemAction;
import it.hurts.shatterbyte.pinatafiesta.data.action.RunCommandAction;
import it.hurts.shatterbyte.pinatafiesta.data.action.WeightedAction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class CommonClass {
    public static void init() {

    }

    public static ItemStack createTestSpawner() {
        ItemStack stack = ModContent.pinataSpawner().getDefaultInstance();
        WeightedAction weightedAction = WeightedAction.builder()
                .add(10, new DropItemAction(Items.DIAMOND.getDefaultInstance()))
                .add(2, new DropItemAction(Items.EMERALD.getDefaultInstance()))
                .add(1, new DropItemAction(Items.GOLD_INGOT.getDefaultInstance()))
                .build();

        PinataDropData dropData = PinataDropData.builder()
                .addSpawnAction(new RunCommandAction("say A Wild Pinata appears!"))
                .addHitAction(weightedAction)
                .addHitAction(weightedAction)
                .addHitAction(weightedAction)
                .addBreakAction(new RunCommandAction("say im stupid"))
                .build();

        stack.set(ModComponents.PINATA_HITS_COMPONENT_TYPE, 5);
        stack.set(ModComponents.DROP_DATA_COMPONENT_TYPE, dropData);
        return stack;
    }
}

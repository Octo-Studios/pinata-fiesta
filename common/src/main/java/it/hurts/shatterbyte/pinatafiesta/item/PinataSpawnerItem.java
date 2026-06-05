package it.hurts.shatterbyte.pinatafiesta.item;

import it.hurts.shatterbyte.pinatafiesta.content.ModComponents;
import it.hurts.shatterbyte.pinatafiesta.content.ModContent;
import it.hurts.shatterbyte.pinatafiesta.content.ModPinataSkins;
import it.hurts.shatterbyte.pinatafiesta.data.PinataTemplate;
import it.hurts.shatterbyte.pinatafiesta.data.PinataTemplateManager;
import it.hurts.shatterbyte.pinatafiesta.entity.PinataEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.DispenserBlock;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class PinataSpawnerItem extends Item {
    private static final DispenseItemBehavior DISPENSE_BEHAVIOR = new DefaultDispenseItemBehavior() {
        @Override
        protected @NonNull ItemStack execute(BlockSource source, @NonNull ItemStack stack) {
            ServerLevel level = source.level();

            Direction direction = source.state().getValue(DispenserBlock.FACING);
            BlockPos spawnPos = source.pos().relative(direction);

            PinataEntity pinata = ModContent.pinataEntity().create(level, null, spawnPos, EntitySpawnReason.DISPENSER, direction != Direction.UP, false);

            if (pinata == null) {
                return stack;
            }

            List<Identifier> skins = stack.get(ModComponents.SKINS_COMPONENT_TYPE);
            if (skins != null && !skins.isEmpty()) {
                pinata.setSkin(ModPinataSkins.getSkin(skins.get(level.getRandom().nextInt(skins.size()))));
            }
            pinata.setDropData(stack.get(ModComponents.DROP_DATA_COMPONENT_TYPE));
            pinata.setHitsLeft(stack.get(ModComponents.PINATA_HITS_COMPONENT_TYPE));

            level.addFreshEntity(pinata);
            pinata.getDropData().executeSpawnActions(level, pinata, null);

            level.playSound(null, pinata.getX(), pinata.getY(), pinata.getZ(), ModContent.pinataSpawnSound(), SoundSource.NEUTRAL, 1.0F, 1.0F);

            stack.shrink(1);
            return stack;
        }
    };

    public PinataSpawnerItem(Properties properties) {
        super(properties);
        DispenserBlock.registerBehavior(this, DISPENSE_BEHAVIOR);
    }

    public static ItemStack fromTemplate(Identifier templateId) {
        PinataTemplate template = PinataTemplateManager.INSTANCE.get(templateId);
        ItemStack stack = ModContent.pinataSpawner().getDefaultInstance();

        stack.set(ModComponents.PINATA_HITS_COMPONENT_TYPE, template.hits());
        stack.set(ModComponents.SKINS_COMPONENT_TYPE, template.skins());
        stack.set(ModComponents.DROP_DATA_COMPONENT_TYPE, template.dropData());
        return stack;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (!(context.getLevel() instanceof ServerLevel serverLevel)) {
            return InteractionResult.SUCCESS;
        }

        Player player = context.getPlayer();

        if (player == null) {
            return InteractionResult.PASS;
        }

        ItemStack stack = context.getItemInHand();
        BlockPos spawnPos = context.getClickedPos().relative(context.getClickedFace());

        PinataEntity pinata = ModContent.pinataEntity().create(serverLevel, null, spawnPos, EntitySpawnReason.SPAWN_ITEM_USE, true, true);

        if (pinata == null) {
            return InteractionResult.FAIL;
        }

        List<Identifier> skins = stack.get(ModComponents.SKINS_COMPONENT_TYPE);
        Identifier skinId = skins.get(player.getRandom().nextInt(skins.size()));
        ModPinataSkins.Skin skin = ModPinataSkins.getSkin(skinId);

        pinata.setSkin(skin);
        pinata.setDropData(stack.get(ModComponents.DROP_DATA_COMPONENT_TYPE));
        pinata.setHitsLeft(stack.get(ModComponents.PINATA_HITS_COMPONENT_TYPE));

        player.level().addFreshEntity(pinata);
        pinata.getDropData().executeSpawnActions(serverLevel, pinata, player);

        serverLevel.playSound(null, pinata.getX(), pinata.getY(), pinata.getZ(), ModContent.pinataSpawnSound(), SoundSource.NEUTRAL, 1.0F, 1.0F);

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        return InteractionResult.CONSUME;
    }
}

package it.hurts.shatterbyte.pinatafiesta.item;

import it.hurts.shatterbyte.pinatafiesta.content.ModComponents;
import it.hurts.shatterbyte.pinatafiesta.content.ModContent;
import it.hurts.shatterbyte.pinatafiesta.content.ModPinataSkins;
import it.hurts.shatterbyte.pinatafiesta.entity.PinataEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;

import java.util.List;

public class PinataSpawnerItem extends Item {
    public PinataSpawnerItem(Properties properties) {
        super(properties);
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

        player.level().addFreshEntity(pinata);

        serverLevel.playSound(null, pinata.getX(), pinata.getY(), pinata.getZ(), ModContent.pinataSpawnSound(), SoundSource.NEUTRAL, 1.0F, 1.0F);

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        return InteractionResult.CONSUME;
    }
}

package it.hurts.shatterbyte.pinatafiesta.item;

import it.hurts.shatterbyte.pinatafiesta.content.ModComponents;
import it.hurts.shatterbyte.pinatafiesta.content.ModContent;
import it.hurts.shatterbyte.pinatafiesta.content.ModPinataSkins;
import it.hurts.shatterbyte.pinatafiesta.data.PinataTemplate;
import it.hurts.shatterbyte.pinatafiesta.data.PinataTemplateManager;
import it.hurts.shatterbyte.pinatafiesta.entity.PinataEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class PinataSpawnerItem extends Item {
    public PinataSpawnerItem(Properties properties) {
        super(properties);
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
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!(level instanceof ServerLevel serverLevel))
            return InteractionResult.SUCCESS;

        Vec3 look = player.getLookAngle();
        Vec3 spawnPos = player.position().add(look).add(0F, player.getBbHeight() / 3F, 0F);
        BlockPos spawnBlockPos = BlockPos.containing(spawnPos);

        PinataEntity pinata = ModContent.pinataEntity().create(serverLevel, null, spawnBlockPos, EntitySpawnReason.SPAWN_ITEM_USE, false, false);

        if (pinata == null)
            return InteractionResult.FAIL;

        List<Identifier> skins = stack.get(ModComponents.SKINS_COMPONENT_TYPE);
        Identifier skinId = skins.get(player.getRandom().nextInt(skins.size()));
        ModPinataSkins.Skin skin = ModPinataSkins.getSkin(skinId);


        pinata.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
        pinata.setDeltaMovement(look.scale(0.35D));
        pinata.setYRot(player.getYRot());
        pinata.setSkin(skin);
        pinata.setDropData(stack.get(ModComponents.DROP_DATA_COMPONENT_TYPE));
        pinata.setHitsLeft(stack.get(ModComponents.PINATA_HITS_COMPONENT_TYPE));
        pinata.setXRot(0F);

        player.level().addFreshEntity(pinata);
        pinata.getDropData().executeSpawnActions(serverLevel, pinata, player);
        serverLevel.addFreshEntity(pinata);

        serverLevel.playSound(null, pinata.getX(), pinata.getY(), pinata.getZ(), ModContent.pinataSpawnSound(), SoundSource.NEUTRAL, 1.0F, 1.0F);
        serverLevel.sendParticles(skin.getPaperParticle(), pinata.getX(), pinata.getY(0.65D), pinata.getZ(), 24, 0.2D, 0.2D, 0.2D, 0.08D);

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        return InteractionResult.CONSUME;
    }
}

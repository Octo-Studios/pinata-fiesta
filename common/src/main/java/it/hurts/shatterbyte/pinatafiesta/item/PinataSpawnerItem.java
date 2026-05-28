package it.hurts.shatterbyte.pinatafiesta.item;

import it.hurts.shatterbyte.pinatafiesta.content.ModComponents;
import it.hurts.shatterbyte.pinatafiesta.content.ModContent;
import it.hurts.shatterbyte.pinatafiesta.content.ModPinataSkins;
import it.hurts.shatterbyte.pinatafiesta.data.PinataTemplate;
import it.hurts.shatterbyte.pinatafiesta.data.PinataTemplateManager;
import it.hurts.shatterbyte.pinatafiesta.entity.PinataEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

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
        Vec3 origin = player.position().add(0D, player.getBbHeight() / 3D, 0D);
        double hitDistance = getBlockHitDistance(serverLevel, player, origin, look);
        double spawnDistance = Math.min(1D, Math.max(0.2D, hitDistance - 0.75D));
        Vec3 spawnPos = origin.add(look.scale(spawnDistance));

        PinataEntity pinata = ModContent.pinataEntity().create(serverLevel, null, BlockPos.containing(spawnPos), EntitySpawnReason.SPAWN_ITEM_USE, false, false);

        if (pinata == null)
            return InteractionResult.FAIL;

        List<Identifier> skins = stack.get(ModComponents.SKINS_COMPONENT_TYPE);
        Identifier skinId = skins.get(player.getRandom().nextInt(skins.size()));
        ModPinataSkins.Skin skin = ModPinataSkins.getSkin(skinId);

        spawnPos = findFreeSpawnPos(serverLevel, pinata, origin, look, spawnDistance);

        if (spawnPos == null)
            return InteractionResult.FAIL;

        spawnDistance = spawnPos.distanceTo(origin);
        double launchSpeed = Math.min(0.35D, Math.max(0.0D, hitDistance - spawnDistance - 0.75D) * 0.5D);
        float spawnYaw = player.getYRot();

        pinata.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
        pinata.setOldPosAndRot(spawnPos, spawnYaw, 0F);
        pinata.setDeltaMovement(look.scale(launchSpeed));
        pinata.setYRot(spawnYaw);
        pinata.setYBodyRot(spawnYaw);
        pinata.setYHeadRot(spawnYaw);
        pinata.setSkin(skin);
        pinata.setDropData(stack.get(ModComponents.DROP_DATA_COMPONENT_TYPE));
        pinata.setHitsLeft(stack.get(ModComponents.PINATA_HITS_COMPONENT_TYPE));
        pinata.setXRot(0F);

        serverLevel.addFreshEntity(pinata);

        pinata.getDropData().executeSpawnActions(serverLevel, pinata, player);

        serverLevel.playSound(null, pinata.getX(), pinata.getY(), pinata.getZ(), ModContent.pinataSpawnSound(), SoundSource.NEUTRAL, 1.0F, 1.0F);
        serverLevel.sendParticles(skin.getPaperParticle(), pinata.getX(), pinata.getY(0.65D), pinata.getZ(), 24, 0.2D, 0.2D, 0.2D, 0.08D);

        if (!player.getAbilities().instabuild)
            stack.shrink(1);

        return InteractionResult.CONSUME;
    }

    private static double getBlockHitDistance(ServerLevel level, Player player, Vec3 origin, Vec3 look) {
        BlockHitResult hit = level.clip(new ClipContext(origin, origin.add(look.scale(3D)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));

        if (hit.getType() != HitResult.Type.BLOCK)
            return 3.0D;

        return hit.getLocation().distanceTo(origin);
    }

    private static Vec3 findFreeSpawnPos(ServerLevel level, PinataEntity pinata, Vec3 origin, Vec3 look, double spawnDistance) {
        double distance = spawnDistance;

        while (distance >= 0.2D) {
            Vec3 pos = origin.add(look.scale(distance));
            pinata.setPos(pos.x, pos.y, pos.z);

            if (level.noCollision(pinata))
                return pos;

            distance -= 0.1D;
        }

        return null;
    }
}

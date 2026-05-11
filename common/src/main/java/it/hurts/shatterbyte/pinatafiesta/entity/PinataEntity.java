package it.hurts.shatterbyte.pinatafiesta.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

public class PinataEntity extends LivingEntity {
    private static final int HITS_TO_BREAK = 100;
    private static final EntityDataAccessor<Integer> DATA_HIT_COUNTER = SynchedEntityData.defineId(PinataEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> DATA_HIT_DIR_X = SynchedEntityData.defineId(PinataEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DATA_HIT_DIR_Z = SynchedEntityData.defineId(PinataEntity.class, EntityDataSerializers.FLOAT);

    private int hits;

    public PinataEntity(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, HITS_TO_BREAK)
                .add(Attributes.GRAVITY, 0.03);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_HIT_COUNTER, 0);
        builder.define(DATA_HIT_DIR_X, 0.0F);
        builder.define(DATA_HIT_DIR_Z, 0.0F);
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        if (isInvulnerableTo(level, source)) {
            return false;
        }

        hits++;
        cacheHitDirection(source);
        getEntityData().set(DATA_HIT_COUNTER, getEntityData().get(DATA_HIT_COUNTER) + 1);
        markHurt();

        if (hits >= HITS_TO_BREAK) {
            breakOpen(level);
            return true;
        }

        level.playSound(null, getX(), getY(), getZ(), SoundEvents.WOOL_HIT, SoundSource.NEUTRAL, 0.9F, 0.8F + random.nextFloat() * 0.35F);
        return true;
    }

    public int getHitCounter() {
        return getEntityData().get(DATA_HIT_COUNTER);
    }

    public float getHitDirX() {
        return getEntityData().get(DATA_HIT_DIR_X);
    }

    public float getHitDirZ() {
        return getEntityData().get(DATA_HIT_DIR_Z);
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("Hits", hits);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        hits = input.getIntOr("Hits", 0);
    }

    private void breakOpen(ServerLevel level) {
        level.playSound(null, getX(), getY(), getZ(), SoundEvents.WOOL_BREAK, SoundSource.NEUTRAL, 1.0F, 0.9F + random.nextFloat() * 0.2F);
        level.playSound(null, getX(), getY(), getZ(), SoundEvents.BUNDLE_DROP_CONTENTS, SoundSource.NEUTRAL, 0.9F, 1.0F);

        dropReward(level, new ItemStack(Items.EXPERIENCE_BOTTLE, 2 + random.nextInt(4)));
        dropReward(level, new ItemStack(Items.COOKIE, 4 + random.nextInt(5)));
        dropReward(level, new ItemStack(Items.IRON_INGOT, 2 + random.nextInt(4)));

        if (random.nextFloat() < 0.45F) {
            dropReward(level, new ItemStack(Items.GOLD_INGOT, 1 + random.nextInt(3)));
        }

        if (random.nextFloat() < 0.2F) {
            dropReward(level, new ItemStack(Items.DIAMOND));
        }

        discard();
    }

    private void cacheHitDirection(DamageSource source) {
        Vec3 sourcePos = source.getSourcePosition();

        if (sourcePos == null && source.getEntity() != null) {
            sourcePos = source.getEntity().position();
        }

        if (sourcePos == null) {
            return;
        }

        double x = getX() - sourcePos.x;
        double z = getZ() - sourcePos.z;
        double length = Math.sqrt(x * x + z * z);

        if (length < 1.0E-4D) {
            return;
        }

        getEntityData().set(DATA_HIT_DIR_X, (float) (x / length));
        getEntityData().set(DATA_HIT_DIR_Z, (float) (z / length));
    }

    private void dropReward(ServerLevel level, ItemStack stack) {
        var item = spawnAtLocation(level, stack);

        if (item != null) {
            item.setDeltaMovement(
                    (random.nextDouble() - 0.5D) * 0.35D,
                    0.25D + random.nextDouble() * 0.25D,
                    (random.nextDouble() - 0.5D) * 0.35D
            );
        }
    }
}

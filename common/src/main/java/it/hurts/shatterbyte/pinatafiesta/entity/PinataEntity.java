package it.hurts.shatterbyte.pinatafiesta.entity;

import it.hurts.shatterbyte.pinatafiesta.Constants;
import it.hurts.shatterbyte.pinatafiesta.content.ModContent;
import it.hurts.shatterbyte.pinatafiesta.content.ModPinataSkins;
import it.hurts.shatterbyte.pinatafiesta.data.PinataDropData;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class PinataEntity extends LivingEntity {
    private static final EntityDataAccessor<String> DATA_SKIN = SynchedEntityData.defineId(PinataEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> DATA_HIT_COUNTER = SynchedEntityData.defineId(PinataEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> DATA_HIT_DIR_X = SynchedEntityData.defineId(PinataEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DATA_HIT_DIR_Z = SynchedEntityData.defineId(PinataEntity.class, EntityDataSerializers.FLOAT);

    private PinataDropData dropData = PinataDropData.EMPTY;
    private ModPinataSkins.Skin skin = ModPinataSkins.SUNSET;
    private int hitsLeft = 10;

    public PinataEntity(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 2)
                .add(Attributes.GRAVITY, 0.02)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_SKIN, Constants.MOD_ID + ":sunset");
        builder.define(DATA_HIT_COUNTER, 0);
        builder.define(DATA_HIT_DIR_X, 0.0F);
        builder.define(DATA_HIT_DIR_Z, 0.0F);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        super.onSyncedDataUpdated(accessor);

        if (DATA_SKIN.equals(accessor)) {
            this.skin = ModPinataSkins.getSkin(
                    Identifier.parse(getEntityData().get(DATA_SKIN))
            );
        }
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        if (this.isInvulnerableTo(level, source)) {
            return false;
        }

        Player player = null;
        if (source.getEntity() instanceof Player) {
            player = (Player) source.getDirectEntity();
        }

        if (this.hurtTime > 0 && player == null) {
            return false;
        }

        hitsLeft--;
        this.cacheHitDirection(source);
        this.getEntityData().set(DATA_HIT_COUNTER, getEntityData().get(DATA_HIT_COUNTER) + 1);
        this.markHurt();

        level.sendParticles(skin.getPaperParticle(), getX(), getY(0.7f), getZ(), 2 + random.nextInt(4), 0.2D, 0.2D, 0.2D, 0.125D);


        dropData.executeHitActions(level, this, player);
        if (hitsLeft <= 0) {
            this.breakOpen(level, player);
            return true;
        }

        this.hurtDuration = 10;
        this.hurtTime = this.hurtDuration;
        this.invulnerableTime = 20;

        level.playSound(null, getX(), getY(), getZ(), ModContent.pinataHurtSound(), SoundSource.NEUTRAL, 1.0F, 0.95F + random.nextFloat() * 0.2F);
        return true;
    }

    public void setSkin(ModPinataSkins.Skin skin) {
        this.skin = skin;
        this.getEntityData().set(DATA_SKIN, skin.id.toString());
    }

    public ModPinataSkins.Skin getSkin() {
        if (skin == null) {
            skin = ModPinataSkins.getSkin(
                    Identifier.parse(getEntityData().get(DATA_SKIN))
            );
        }

        return skin;
    }

    public void setDropData(PinataDropData dropData) {
        this.dropData = dropData;
    }

    public PinataDropData getDropData() {
        return this.dropData;
    }

    public void setHitsLeft(int hitsLeft) {
        this.hitsLeft = hitsLeft;
    }

    public int getHitsLeft() {
        return this.hitsLeft;
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
        output.putString("skin", getEntityData().get(DATA_SKIN));
        output.putInt("hits_left", hitsLeft);
        output.store("drop_data", PinataDropData.CODEC, dropData);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);

        String skinId = input.getStringOr("skin", Constants.MOD_ID + ":sunset");

        getEntityData().set(DATA_SKIN, skinId);
        skin = ModPinataSkins.getSkin(Identifier.parse(skinId));

        hitsLeft = input.getIntOr("hits_left", 10);

        dropData = input.read("drop_data", PinataDropData.CODEC).orElse(PinataDropData.EMPTY);
    }

    private void breakOpen(ServerLevel level, @Nullable Player player) {
        level.playSound(null, getX(), getY(), getZ(), ModContent.pinataDeathSound(), SoundSource.NEUTRAL, 1.1F, 0.95F + random.nextFloat() * 0.1F);
        level.sendParticles(skin.getConfettiParticle(), getX(), getY(0.65D), getZ(), 256, 0.25D, 0.25D, 0.25D, 0.275D);
        dropData.executeBreakActions(level, this, player);
        this.discard();
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
}

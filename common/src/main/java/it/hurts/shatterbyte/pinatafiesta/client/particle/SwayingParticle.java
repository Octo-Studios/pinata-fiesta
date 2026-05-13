package it.hurts.shatterbyte.pinatafiesta.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public abstract class SwayingParticle extends SingleQuadParticle {
    private final float spinSpeed;
    private final float swaySpeed;
    private final float swayStrength;

    protected SwayingParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, float spinSpeed, float swaySpeed, float swayStrength, TextureAtlasSprite sprite, RandomSource random) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, sprite);
        this.spinSpeed = spinSpeed * (random.nextFloat() - 0.5F);
        this.swaySpeed = swaySpeed + random.nextFloat() * 0.08F;;
        this.swayStrength = swayStrength + random.nextFloat() * 0.004F;
    }

    @Override
    public Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.removed) {
            return;
        }


        this.oRoll = this.roll;
        if (!this.onGround) {
            this.roll += this.spinSpeed;

            float swayStart = this.lifetime * 0.2F;
            float swayMax = this.lifetime * 0.5F;

            if (this.yd < 0.0D && this.age >= swayStart) {
                float swayProgress = Mth.clamp((this.age - swayStart) / (swayMax - swayStart), 0.0F, 1.0F);
                float currentSwayStrength = this.swayStrength * swayProgress;

                this.xd += Math.sin((this.age + this.swaySpeed) * this.swaySpeed) * currentSwayStrength;
                this.zd += Math.cos((this.age + this.swaySpeed) * this.swaySpeed) * currentSwayStrength;
            }
        }

        float fadeStart = this.lifetime * 0.75F;
        if (this.age > fadeStart) {
            setAlpha(1.0F - (this.age - fadeStart) / (this.lifetime - fadeStart));
        }
    }
}

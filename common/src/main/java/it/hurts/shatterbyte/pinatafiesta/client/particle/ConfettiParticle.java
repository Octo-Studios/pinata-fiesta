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

public class ConfettiParticle extends SingleQuadParticle {
    private final float spinSpeed;
    private final float swaySpeed;
    private final float swayStrength;

    protected ConfettiParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, TextureAtlasSprite sprite, RandomSource random) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, sprite);
        this.xd = xSpeed + (random.nextDouble() - 0.5D) * 0.08D;
        this.yd = ySpeed + 0.025d + random.nextDouble() * 0.08D;
        this.zd = zSpeed + (random.nextDouble() - 0.5D) * 0.08D;
        this.quadSize = 0.075F + random.nextFloat() * 0.045F;
        this.lifetime = 100 + random.nextInt(35);
        this.gravity = 0.05F + random.nextFloat() * 0.012F;
        this.friction = 0.85F;
        this.roll = random.nextFloat() * Mth.TWO_PI;
        this.oRoll = this.roll;
        this.spinSpeed = (random.nextFloat() - 0.5F) * 0.45F;
        this.swaySpeed = 0.05F + random.nextFloat() * 0.08F;
        this.swayStrength = 0.003F + random.nextFloat() * 0.004F;
        setSize(0.04F, 0.04F);
    }

    @Override
    public SingleQuadParticle.Layer getLayer() {
        return SingleQuadParticle.Layer.TRANSLUCENT;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.removed) {
            return;
        }

        this.oRoll = this.roll;
        this.roll += this.spinSpeed;
        this.xd += Math.sin((this.age + this.swaySpeed) * this.swaySpeed) * this.swayStrength;
        this.zd += Math.cos((this.age + this.swaySpeed) * this.swaySpeed) * this.swayStrength;

        float fadeStart = this.lifetime * 0.65F;
        if (this.age > fadeStart) {
            setAlpha(1.0F - (this.age - fadeStart) / (this.lifetime - fadeStart));
        }
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            return new ConfettiParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites.get(random), random);
        }
    }
}

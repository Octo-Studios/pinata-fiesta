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

public class PaperParticle extends SwayingParticle {
    protected PaperParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, TextureAtlasSprite sprite, RandomSource random) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, 0.45f, 0.05f, 0.003f, sprite, random);
        this.xd = xSpeed + (random.nextDouble() - 0.5D) * 0.01D;
        this.yd = ySpeed + 0.01d + random.nextDouble() * 0.01D;
        this.zd = zSpeed + (random.nextDouble() - 0.5D) * 0.01D;
        this.quadSize = 0.075F + random.nextFloat() * 0.045F;
        this.lifetime = 40 + random.nextInt(35);
        this.gravity = 0.25F + random.nextFloat() * 0.012F;
        this.friction = 0.94F;
        this.roll = random.nextFloat() * Mth.TWO_PI;
        this.oRoll = this.roll;
        setSize(0.06F, 0.06F);
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            return new PaperParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites.get(random), random);
        }
    }
}

package it.hurts.shatterbyte.pinatafiesta.content;

import it.hurts.shatterbyte.pinatafiesta.Constants;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.Identifier;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class ModPinataSkins {
    public static final Set<Identifier> SKINS = new HashSet<>();

    public static final Skin SUNSET = registerSkin(
            Constants.id("sunset"),
            ModContent::confettiParticle,
            ModContent::paperParticle
    );

    public static Skin registerSkin(Identifier skinId, Supplier<SimpleParticleType> confettiParticle, Supplier<SimpleParticleType> paperParticle) {
        Skin skin = new Skin(skinId, confettiParticle, paperParticle);
        SKINS.add(skinId);
        return skin;
    }

    public static class Skin {
        public final Identifier id;
        private final Supplier<SimpleParticleType> confettiParticle;
        private final Supplier<SimpleParticleType> paperParticle;

        public Skin(Identifier skinId, Supplier<SimpleParticleType> confettiParticle, Supplier<SimpleParticleType> paperParticle) {
            this.id = skinId;
            this.confettiParticle = confettiParticle;
            this.paperParticle = paperParticle;
        }

        public SimpleParticleType getConfettiParticle() {
            return confettiParticle.get();
        }

        public SimpleParticleType getPaperParticle() {
            return paperParticle.get();
        }
    }
}

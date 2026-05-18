package it.hurts.shatterbyte.pinatafiesta.content;

import it.hurts.shatterbyte.pinatafiesta.Constants;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.Identifier;

import java.util.*;
import java.util.function.Supplier;

public class ModPinataSkins {
    private static final Map<Identifier, Skin> SKINS = new HashMap<>();

    public static final Skin SUNSET = registerSkin(
            Constants.id("sunset"),
            ModContent::sunsetConfetti,
            ModContent::sunsetPaper
    );

    public static final Skin AQUAMARINE = registerSkin(
            Constants.id("aquamarine"),
            ModContent::aquamarineConfetti,
            ModContent::aquamarinePaper
    );

    public static Skin registerSkin(Identifier skinId, Supplier<SimpleParticleType> confettiParticle, Supplier<SimpleParticleType> paperParticle) {
        Skin skin = new Skin(skinId, confettiParticle, paperParticle);
        SKINS.put(skinId, skin);
        return skin;
    }

    public static Skin getSkin(Identifier skinId) {
        return SKINS.get(skinId);
    }

    public static List<Skin> getSkins() {
        return new ArrayList<>(SKINS.values());
    }

    public static List<Identifier> getSkinIds() {
        return new ArrayList<>(SKINS.keySet());
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

package it.hurts.shatterbyte.pinatafiesta.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import it.hurts.shatterbyte.pinatafiesta.Constants;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PinataTemplateManager extends SimpleJsonResourceReloadListener<PinataTemplate> {
    public static final PinataTemplateManager INSTANCE = new PinataTemplateManager();
    public static final Identifier ID = Constants.id("pinata_templates");

    private Map<Identifier, PinataTemplate> templates = Map.of();

    public PinataTemplateManager() {
        super(
                PinataTemplate.CODEC,
                FileToIdConverter.json("pinata_templates")
        );
    }

    @Override
    protected void apply(
            Map<Identifier, PinataTemplate> object,
            ResourceManager resourceManager,
            ProfilerFiller profilerFiller
    ) {
        this.templates = Map.copyOf(object);

        Constants.LOG.info(
                "Loaded {} pinata templates",
                templates.size()
        );
    }

    public PinataTemplate get(Identifier id) {
        return templates.getOrDefault(id, PinataTemplate.DEFAULT);
    }

    public boolean contains(Identifier id) {
        return templates.containsKey(id);
    }

    public Set<Identifier> getIds() {
        return templates.keySet();
    }
}
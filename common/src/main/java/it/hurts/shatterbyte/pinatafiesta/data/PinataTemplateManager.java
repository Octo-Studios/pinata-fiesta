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

public class PinataTemplateManager extends SimpleJsonResourceReloadListener<PinataDropData> {
    public static final PinataTemplateManager INSTANCE =
            new PinataTemplateManager();

    private Map<Identifier, PinataDropData> templates = Map.of();

    public PinataTemplateManager() {
        super(
                PinataDropData.CODEC,
                FileToIdConverter.json("pinata_templates")
        );
    }

    @Override
    protected void apply(
            Map<Identifier, PinataDropData> object,
            ResourceManager resourceManager,
            ProfilerFiller profilerFiller
    ) {
        this.templates = Map.copyOf(object);

        Constants.LOG.info(
                "Loaded {} pinata templates",
                templates.size()
        );
    }

    public PinataDropData get(Identifier id) {
        return templates.getOrDefault(id, PinataDropData.EMPTY);
    }

    public boolean contains(Identifier id) {
        return templates.containsKey(id);
    }

    public Set<Identifier> getIds() {
        return templates.keySet();
    }
}
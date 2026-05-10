package it.hurts.shatterbyte.pinatafiesta.content;

import it.hurts.shatterbyte.pinatafiesta.Constants;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.Identifier;

public class ModEntityModelLayers {
	public static final ModelLayerLocation PINATA = createMain("pinata");

	private static ModelLayerLocation createMain(String name) {
		return new ModelLayerLocation(Identifier.fromNamespaceAndPath(Constants.MOD_ID, name), "main");
	}
}
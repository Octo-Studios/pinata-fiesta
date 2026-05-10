package it.hurts.shatterbyte.pinatafiesta;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.resources.Identifier;

public class Constants {
	public static final String MOD_ID = "pinatafiesta";
	public static final String MOD_NAME = "Pinata Fiesta";
	public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}

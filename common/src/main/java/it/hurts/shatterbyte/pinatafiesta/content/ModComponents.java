package it.hurts.shatterbyte.pinatafiesta.content;

import it.hurts.shatterbyte.pinatafiesta.Constants;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

import java.util.List;

public class ModComponents {
	public static final DataComponentType<List<Identifier>> SKINS_COMPONENT_TYPE = Registry.register(
			BuiltInRegistries.DATA_COMPONENT_TYPE,
			Identifier.fromNamespaceAndPath(Constants.MOD_ID, "skins"),
			DataComponentType.<List<Identifier>>builder().persistent(Identifier.CODEC.listOf()).build()
	);
}
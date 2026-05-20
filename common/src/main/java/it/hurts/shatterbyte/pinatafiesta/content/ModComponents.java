package it.hurts.shatterbyte.pinatafiesta.content;

import it.hurts.shatterbyte.pinatafiesta.Constants;
import it.hurts.shatterbyte.pinatafiesta.data.PinataDropData;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

import java.util.List;

public class ModComponents {
	public static final DataComponentType<List<Identifier>> SKINS_COMPONENT_TYPE = Registry.register(
			BuiltInRegistries.DATA_COMPONENT_TYPE,
			Constants.id("skins"),
			DataComponentType.<List<Identifier>>builder().persistent(Identifier.CODEC.listOf()).build()
	);

	public static final DataComponentType<PinataDropData> DROP_DATA_COMPONENT_TYPE = Registry.register(
			BuiltInRegistries.DATA_COMPONENT_TYPE,
			Constants.id("drop_data"),
			DataComponentType.<PinataDropData>builder().persistent(PinataDropData.CODEC).build()
	);
}
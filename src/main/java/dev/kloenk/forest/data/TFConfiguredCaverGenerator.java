package dev.kloenk.forest.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import java.nio.file.Path;

public class TFConfiguredCaverGenerator extends TFWorldgenGenerator<ConfiguredCarver<?>> {


    public TFConfiguredCaverGenerator(DataGenerator root) {
        super(root, ConfiguredCarver.CODEC, BuiltinRegistries.CONFIGURED_CARVER);

    }

    @Override
    protected Path getPath(Path root, Identifier id) {
        return root.resolve("data/" + id.getNamespace() + "/worldgen/configured_carver/" + id.getPath() + ".json");
    }

    @Override
    public String getName() {
        return "Configured WorldGen cavers";
    }
}

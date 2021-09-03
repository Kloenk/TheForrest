package dev.kloenk.forest.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import java.nio.file.Path;

public class TFConfiguredFeatureGenerater extends TFWorldgenGenerator<ConfiguredFeature<?, ?>> {


    public TFConfiguredFeatureGenerater(DataGenerator root) {
        super(root, ConfiguredFeature.CODEC, BuiltinRegistries.CONFIGURED_FEATURE);

    }

    @Override
    protected Path getPath(Path root, Identifier id) {
        return root.resolve("data/" + id.getNamespace() + "/worldgen/configured_feature/" + id.getPath() + ".json");
    }


    @Override
    public String getName() {
        return "Configured WorldGen features";
    }
}

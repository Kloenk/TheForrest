package dev.kloenk.forest.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import dev.kloenk.forest.world.dimension.biom.feature.TFConfiguredFeatures;
import net.minecraft.data.DataCache;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.intellij.lang.annotations.JdkConstants;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Function;

public class TFConfiguredFeatureGenerater implements DataProvider {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    protected final DataGenerator root;
    protected final Registry<ConfiguredFeature<?, ?>> registry;


    public TFConfiguredFeatureGenerater(DataGenerator root) {
        this.root = root;
        this.registry = BuiltinRegistries.CONFIGURED_FEATURE;

    }

    @Override
    public void run(DataCache cache) throws IOException {
        Path path = this.root.getOutput();
        Iterator features = this.registry.getEntries().iterator();

        while (features.hasNext()) {
            Map.Entry<RegistryKey<ConfiguredFeature>, ConfiguredFeature> entry = (Map.Entry)features.next();
            Path out = getPath(path, ((RegistryKey)entry.getKey()).getValue());
            ConfiguredFeature feature = (ConfiguredFeature)entry.getValue() ;
            Function function = JsonOps.INSTANCE.withEncoder(ConfiguredFeature.CODEC);

            try {
                /*Optional<JsonElement> optional = ((DataResult)function.apply(() -> {
                    return feature;
                })).result();*/
                Optional<JsonElement> optional = ((DataResult)function.apply(feature)).result();
                if (optional.isPresent()) {
                    DataProvider.writeToPath(GSON, cache, (JsonElement) optional.get(), out);
                } else {
                    LOGGER.error((String)"Couldn't serialize feature {}", (Object)out);
                }
            } catch (IOException e) {
                LOGGER.error((String) "Couldn't save feature {}", (Object) out, (Object) e);
            }
        }
    }

    private static Path getPath(Path root, Identifier id) {
        return root.resolve("data/" + id.getNamespace() + "/worldgen/configured_feature/" + id.getPath() + ".json");
    }


    @Override
    public String getName() {
        return "Configured WorlGen features";
    }
}

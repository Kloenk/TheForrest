package dev.kloenk.forest.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.data.DataCache;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public abstract class TFWorldgenGenerator<T> implements DataProvider {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    protected final DataGenerator root;
    protected final Registry<T> registry;
    protected final Codec<?> CODEC;

    protected TFWorldgenGenerator(DataGenerator root, Codec<?> codec, Registry<T> registry) {
        this.root = root;
        CODEC = codec;
        this.registry = registry;
    }

    @Override
    public void run(DataCache cache) throws IOException {
        Path path = this.root.getOutput();
        Iterator features = this.registry.getEntries().iterator();

        while (features.hasNext()) {
            Map.Entry<RegistryKey<T>, T> entry = (Map.Entry)features.next();
            Path out = getPath(path, entry.getKey().getValue());
            T feature = entry.getValue();
            Function function = JsonOps.INSTANCE.withEncoder(CODEC);

            try {
                Optional<JsonElement> optional = ((DataResult)function.apply(feature)).result();
                if (optional.isPresent()) {
                    DataProvider.writeToPath(GSON, cache, optional.get(), out);
                } else {
                    LOGGER.error("Couldn't serialize feature {}", (Object)out);
                }
            } catch (IOException e) {
                LOGGER.error("Couldn't save feature {}", (Object) out, (Object) e);
            }
        }
    }

    protected abstract Path getPath(Path root, Identifier id);

    public abstract String getName();
}

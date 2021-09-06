package dev.kloenk.forest.data;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dev.kloenk.forest.item.TFItems;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.data.DataCache;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonFactory;
import net.minecraft.item.ItemConvertible;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.crypto.Data;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

// Based on RecipiesProvider
public class TFRecipesGenerator implements DataProvider {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

    protected final DataGenerator root;
    public TFRecipesGenerator(DataGenerator root) {
        this.root = root;
    }

    @Override
    public void run(DataCache cache){
        Set<Identifier> set = Sets.newHashSet();
        configure((recipeJsonProvider -> {
            if (!set.add(recipeJsonProvider.getRecipeId())) {
                throw new IllegalStateException("Duplicate recipe " + recipeJsonProvider.getRecipeId());
            } else {
                JsonObject json = recipeJsonProvider.toJson();;
                saveRecipe(cache, json, recipeJsonProvider.getRecipeId());
                JsonObject jsonAdvancement = recipeJsonProvider.toAdvancementJson();
                if (jsonAdvancement != null) {
                    saveRecipeAdvancement(cache, jsonAdvancement, recipeJsonProvider.getAdvancementId());
                }
            }
        }));
    }

    protected void configure(Consumer<RecipeJsonProvider> exporter) {
        // Naga
        // TODO: enchanted result
        ShapedRecipeJsonFactory.create(TFItems.NAGA_CHESTPLATE)
                .input('X', TFItems.NAGA_SCALE)
                .pattern("X X")
                .pattern("XXX")
                .pattern("XXX")
                .criterion("has_naga_scale", conditionsFromItem(TFItems.NAGA_SCALE))
                .offerTo(exporter);
        // TODO: enchanted result
        ShapedRecipeJsonFactory.create(TFItems.NAGA_LEGGINS)
                .input('X', TFItems.NAGA_SCALE)
                .pattern("XXX")
                .pattern("X X")
                .pattern("X X")
                .criterion("has_naga_scale", conditionsFromItem(TFItems.NAGA_SCALE))
                .offerTo(exporter);

    }

    protected void saveRecipe(DataCache cache, JsonObject json, Identifier id) {
        Path path = getPath(id);
        try {
            DataProvider.writeToPath(GSON, cache, json, path);
        } catch (IOException e) {
            LOGGER.error("Couldn't save recipe {}", id, e);
        }
    }

    protected void saveRecipeAdvancement(DataCache cache, JsonObject json, Identifier id) {
        Path path = getAdvancementPath(id);
        try {
            DataProvider.writeToPath(GSON, cache, json, path);
        } catch (IOException e) {
            LOGGER.error("Couldn't save recipe advancement {}", id, e);
        }
    }

    protected Path getPath(Identifier id) {
         return getPath(id.getNamespace(), id.getPath());
    }

    protected Path getPath(String namespace, String path) {
        Path rootPath = this.root.getOutput();
        return rootPath.resolve("data/" + namespace + "/recipes/" + path + ".json");
    }

    protected Path getAdvancementPath(Identifier id) {
        return getAdvancementPath(id.getNamespace(), id.getPath());
    }

    protected Path getAdvancementPath(String namespace, String path) {
        Path rootPath = this.root.getOutput();
        return rootPath.resolve("data/" + namespace + "/advancements/" + path + ".json");
    }

    @Override
    public String getName() {
        return "TheForrest recipes provider";
    }

    // Helpers
    protected InventoryChangedCriterion.Conditions conditionsFromItem(ItemConvertible item) {
        return conditionsFromItemPredicates(ItemPredicate.Builder.create().items(item).build());
    }

    protected InventoryChangedCriterion.Conditions conditionsFromItemPredicates(ItemPredicate... items) {
        return new InventoryChangedCriterion.Conditions(EntityPredicate.Extended.EMPTY, NumberRange.IntRange.ANY, NumberRange.IntRange.ANY, NumberRange.IntRange.ANY, items);
    }
}

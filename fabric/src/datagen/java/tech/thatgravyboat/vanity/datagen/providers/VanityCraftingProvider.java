package tech.thatgravyboat.vanity.datagen.providers;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import tech.thatgravyboat.vanity.common.registries.ModItems;

import java.util.concurrent.CompletableFuture;

public class VanityCraftingProvider extends FabricRecipeProvider {

    public VanityCraftingProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void buildRecipes(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.STYLING_TABLE.get())
                .pattern("@@")
                .pattern("##")
                .pattern("##")
                .define('@', ConventionalItemTags.LEATHERS)
                .define('#', ItemTags.PLANKS)
                .unlockedBy("has_leather", has(ConventionalItemTags.LEATHERS))
                .save(output);
    }
}

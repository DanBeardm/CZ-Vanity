package tech.thatgravyboat.vanity.common.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import tech.thatgravyboat.vanity.api.design.DesignManager;
import tech.thatgravyboat.vanity.common.Vanity;
import tech.thatgravyboat.vanity.common.compat.jei.categories.DesignCategory;
import tech.thatgravyboat.vanity.common.compat.jei.categories.DesignCategoryRecipe;
import tech.thatgravyboat.vanity.common.registries.ModDataComponents;
import tech.thatgravyboat.vanity.common.registries.ModItems;

import java.util.Objects;

@JeiPlugin
public class VanityJeiPlugin implements IModPlugin {

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return Vanity.id("jei");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new DesignCategory(helper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        DesignManager manager = DesignManager.get(true);

        registration.addRecipes(DesignCategory.TYPE, manager.getAllDesigns().entrySet().stream()
                .flatMap(DesignCategoryRecipe::fromDesign)
                .toList());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(ModItems.STYLING_TABLE.get().getDefaultInstance(), DesignCategory.TYPE);
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(
                ModItems.DESIGN.get(),
                (stack, context) -> Objects.toString(stack.get(ModDataComponents.DESIGN.get()))
        );
    }
}

package tech.thatgravyboat.vanity.common.item;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;
import org.jetbrains.annotations.Nullable;
import tech.thatgravyboat.vanity.api.design.Design;
import tech.thatgravyboat.vanity.common.registries.ModDataComponents;

import java.util.List;

public class DesignHelper {

    private static final String TRANSLATION_KEY = "design";

    @Nullable
    public static ResourceLocation getDesign(ItemStack stack) {
        return stack.get(ModDataComponents.DESIGN.get());
    }

    @Nullable
    public static Pair<ResourceLocation, String> getStyle(ItemStack stack) {
        return stack.get(ModDataComponents.STYLE.get());
    }

    public static ItemStack createDesignItem(ResourceLocation id, Design design) {
        ItemStack stack = design.item().copyWithCount(1);
        stack.set(ModDataComponents.DESIGN.get(), id);
        stack.set(DataComponents.ITEM_NAME, DesignHelper.getTranslationKey(id, null));

        // Static description: design.<ns>.<path>.desc
        String descKey = id.toLanguageKey("design") + ".desc";
        stack.set(DataComponents.LORE, new ItemLore(List.of(Component.translatable(descKey))));

        return stack;
    }

    public static void setDesignAndStyle(ItemStack stack, @Nullable ResourceLocation id, @Nullable String style) {
        if (stack.isEmpty()) return;
        if (id == null || style == null) {
            stack.remove(ModDataComponents.STYLE.get());
            return;
        }
        stack.set(ModDataComponents.STYLE.get(), Pair.of(id, style));
    }

    public static MutableComponent getTranslationKey(ResourceLocation design, @Nullable String style) {
        String key = style != null ? design.toLanguageKey(TRANSLATION_KEY, style) : design.toLanguageKey(TRANSLATION_KEY);
        return Component.translatable(key);
    }

}

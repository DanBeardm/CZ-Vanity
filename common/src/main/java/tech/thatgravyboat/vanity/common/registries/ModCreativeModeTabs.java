package tech.thatgravyboat.vanity.common.registries;

import com.teamresourceful.resourcefullib.common.item.tabs.ResourcefulCreativeModeTab;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import tech.thatgravyboat.vanity.api.design.DesignManager;
import tech.thatgravyboat.vanity.common.Vanity;
import tech.thatgravyboat.vanity.common.item.DesignHelper;

import java.util.function.BiConsumer;

public class ModCreativeModeTabs {

    private static final ResourceKey<CreativeModeTab> FUNCTIONAL_BLOCKS = ResourceKey.create(Registries.CREATIVE_MODE_TAB, ResourceLocation.withDefaultNamespace("functional_blocks"));
    public static final ResourcefulRegistry<CreativeModeTab> TABS = ResourcefulRegistries.create(BuiltInRegistries.CREATIVE_MODE_TAB, Vanity.MOD_ID);

    public static final RegistryEntry<CreativeModeTab> DESIGN_TAB = TABS.register("designs",
            () -> new ResourcefulCreativeModeTab(Vanity.id("designs"))
                    .setItemIcon(ModBlocks.STYLING_TABLE)
                    .addContent(() -> {
                        DesignManager manager = DesignManager.get(true);
                        return manager.getAllDesigns()
                                .entrySet()
                                .stream()
                                .filter(entry -> !entry.getValue().type().hideFromCreativeTab())
                                .map(entry -> DesignHelper.createDesignItem(entry.getKey(), entry.getValue()));
                    }).build()
    );

    /**
     * Adds the value after the value specified in the first parameter of the bi-consumer
     */
    public static void setupCreativeTab(ResourceKey<CreativeModeTab> tab, BiConsumer<ItemLike, ItemLike> adder) {
        if (tab.equals(FUNCTIONAL_BLOCKS)) {
            adder.accept(Items.LOOM, ModBlocks.STYLING_TABLE.get());
        }
    }
}

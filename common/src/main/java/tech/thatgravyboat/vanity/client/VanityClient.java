package tech.thatgravyboat.vanity.client;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import tech.thatgravyboat.vanity.client.design.ClientDesignManager;
import tech.thatgravyboat.vanity.client.screen.StorageScreen;
import tech.thatgravyboat.vanity.client.screen.StylingScreen;
import tech.thatgravyboat.vanity.common.registries.ModMenuTypes;

import java.util.function.Consumer;

public class VanityClient {

    public static void registerModels(ResourceManager resourceManager, Consumer<ModelResourceLocation> out) {
        for (ResourceLocation resource : resourceManager.listResources("models/item/" + ClientDesignManager.PATH, name -> name.getPath().endsWith(".json")).keySet()) {
            out.accept(new ModelResourceLocation(
                    ResourceLocation.fromNamespaceAndPath(
                            resource.getNamespace(),
                            resource.getPath().substring(12, resource.getPath().length() - 5)
                    ),
                    "inventory"
            ));
        }
    }

    public static void registerScreens(ScreenRegistrar registrar) {
        registrar.register(ModMenuTypes.STYLING.get(), StylingScreen::new);
        registrar.register(ModMenuTypes.STORAGE.get(), StorageScreen::new);
    }

    public interface ScreenRegistrar {
        <M extends AbstractContainerMenu, U extends Screen & MenuAccess<M>> void register(
                MenuType<? extends M> type, MenuScreens.ScreenConstructor<M, U> factory
        );
    }
}

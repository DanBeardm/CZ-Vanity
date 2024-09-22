package tech.thatgravyboat.vanity.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import tech.thatgravyboat.vanity.datagen.providers.*;

public class VanityDatagen implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        var pack = generator.createPack();

        pack.addProvider(VanityBlockTagsProvider::new);
        pack.addProvider(VanityPoiTagsProvider::new);

        pack.addProvider(VanityLootTableProvider::new);

        pack.addProvider(VanityCraftingProvider::new);

        pack.addProvider(VanityTradesProvider::new);
    }
}

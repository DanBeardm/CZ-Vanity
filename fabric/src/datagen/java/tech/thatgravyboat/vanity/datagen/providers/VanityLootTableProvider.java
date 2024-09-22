package tech.thatgravyboat.vanity.datagen.providers;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup;
import tech.thatgravyboat.vanity.common.registries.ModBlocks;

import java.util.concurrent.CompletableFuture;

public class VanityLootTableProvider extends FabricBlockLootTableProvider {

    public VanityLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        add(ModBlocks.STYLING_TABLE.get(), createSingleItemTable(ModBlocks.STYLING_TABLE.get()));
    }
}

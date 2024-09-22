package tech.thatgravyboat.vanity.datagen.providers;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import tech.thatgravyboat.vanity.common.registries.ModBlocks;

import java.util.concurrent.CompletableFuture;

public class VanityBlockTagsProvider extends FabricTagProvider.BlockTagProvider {

    public VanityBlockTagsProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_AXE)
                .add(ModBlocks.STYLING_TABLE.get());
    }
}

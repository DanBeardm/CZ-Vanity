package tech.thatgravyboat.vanity.datagen.providers;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.PoiTypeTags;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import tech.thatgravyboat.vanity.common.registries.ModProfessions;

import java.util.concurrent.CompletableFuture;

public class VanityPoiTagsProvider extends FabricTagProvider<PoiType> {

    public VanityPoiTagsProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.POINT_OF_INTEREST_TYPE, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        getOrCreateTagBuilder(PoiTypeTags.ACQUIRABLE_JOB_SITE)
                .add(ModProfessions.STYLIST_POI.getId());
    }
}

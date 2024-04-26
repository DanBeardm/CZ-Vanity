package tech.thatgravyboat.vanity.common.registries;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.resourcefullib.common.bytecodecs.ExtraByteCodecs;
import com.teamresourceful.resourcefullib.common.item.ResourcefulComponentType;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import tech.thatgravyboat.vanity.common.Vanity;

public class ModDataComponents {

    public static final ResourcefulRegistry<DataComponentType<?>> TYPES = ResourcefulRegistries.create(BuiltInRegistries.DATA_COMPONENT_TYPE, Vanity.MOD_ID);

    public static final RegistryEntry<DataComponentType<Pair<ResourceLocation, String>>> STYLE = TYPES.register("style", () -> new ResourcefulComponentType<Pair<ResourceLocation, String>>()
            .cacheEncoding()
            .persistent(RecordCodecBuilder.create(instance -> instance.group(
                    ResourceLocation.CODEC.fieldOf("design").forGetter(Pair::getFirst),
                    Codec.STRING.fieldOf("style").forGetter(Pair::getSecond)
            ).apply(instance, Pair::of)))
            .networkSynchronized(ExtraByteCodecs.pair(ExtraByteCodecs.RESOURCE_LOCATION, ByteCodec.STRING))
            .build());

    public static final RegistryEntry<DataComponentType<ResourceLocation>> DESIGN = TYPES.register("design", () -> new ResourcefulComponentType<ResourceLocation>()
            .cacheEncoding()
            .persistent(ResourceLocation.CODEC)
            .networkSynchronized(ExtraByteCodecs.RESOURCE_LOCATION)
            .build());
}

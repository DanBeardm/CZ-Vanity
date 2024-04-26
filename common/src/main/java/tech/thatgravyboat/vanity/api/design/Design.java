package tech.thatgravyboat.vanity.api.design;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import com.teamresourceful.resourcefullib.common.bytecodecs.ExtraByteCodecs;
import com.teamresourceful.resourcefullib.common.codecs.CodecExtras;
import com.teamresourceful.resourcefullib.common.codecs.recipes.ItemStackCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import tech.thatgravyboat.vanity.api.style.Style;
import tech.thatgravyboat.vanity.api.style.StyleListCodec;
import tech.thatgravyboat.vanity.common.registries.ModItems;

import java.util.*;

public record Design(
    @Nullable ResourceLocation model,
    ItemStack item,
    DesignType type,
    Map<String, List<Style>> styles
) {

    public static final Codec<Design> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.optionalFieldOf("model").forGetter(CodecExtras.optionalFor(Design::model)),
            ItemStackCodec.CODEC.optionalFieldOf("item", ModItems.DESIGN.get().getDefaultInstance()).forGetter(Design::item),
            DesignType.CODEC.optionalFieldOf("type", DesignType.ITEM).forGetter(Design::type),
            Codec.unboundedMap(Codec.STRING, StyleListCodec.INSTANCE).fieldOf("styles").forGetter(Design::styles)
    ).apply(instance, Design::new));

    public static final ByteCodec<Design> BYTE_CODEC = ObjectByteCodec.create(
            ExtraByteCodecs.RESOURCE_LOCATION.optionalFieldOf(CodecExtras.optionalFor(Design::model)),
            ExtraByteCodecs.ITEM_STACK.fieldOf(Design::item),
            DesignType.BYTE_CODEC.fieldOf(Design::type),
            ByteCodec.mapOf(ByteCodec.STRING, Style.BYTE_CODEC.listOf()).fieldOf(Design::styles),
            Design::new
    );

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private Design(Optional<ResourceLocation> model, ItemStack item, DesignType type, Map<String, List<Style>> styles) {
        this(model.orElse(null), item, type, clearEmptyLists(styles));
    }

    private static Map<String, List<Style>> clearEmptyLists(Map<String, List<Style>> styles) {
        Map<String, List<Style>> newStyles = new HashMap<>(styles);
        newStyles.values().removeIf(List::isEmpty);
        return newStyles;
    }

    @Nullable
    public Style getStyleForItem(String name, ItemStack stack) {
        List<Style> entries = this.styles.get(name);
        if (entries == null) {
            return null;
        }

        for (Style entry : entries) {
            if (entry.supportsItem(stack)) {
                return entry;
            }
        }

        return null;
    }

    public List<String> getStylesForItem(ItemStack stack) {
        List<String> styles = new ArrayList<>();
        for (Map.Entry<String, List<Style>> entry : this.styles.entrySet()) {
            for (Style style : entry.getValue()) {
                if (style.supportsItem(stack)) {
                    styles.add(entry.getKey());
                    break;
                }
            }
        }
        return styles;
    }

    public boolean canBeSold() {
        return this.type == DesignType.SELLABLE;
    }
}

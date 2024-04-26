package tech.thatgravyboat.vanity.api.design;

import com.mojang.serialization.Codec;
import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.resourcefullib.common.codecs.EnumCodec;

public enum DesignType {
    ITEM,
    SELLABLE,
    DEFAULT,
    HIDDEN,
    ;

    public static final Codec<DesignType> CODEC = EnumCodec.of(DesignType.class);
    public static final ByteCodec<DesignType> BYTE_CODEC = ByteCodec.ofEnum(DesignType.class);

    public boolean hideFromCreativeTab() {
        return this == HIDDEN || this == DEFAULT;
    }

    public boolean isHidden() {
        return this == HIDDEN;
    }

    public boolean isDefault() {
        return this == DEFAULT;
    }
}

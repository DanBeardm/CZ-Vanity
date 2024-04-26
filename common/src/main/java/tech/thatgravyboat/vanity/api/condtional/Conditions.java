package tech.thatgravyboat.vanity.api.condtional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import tech.thatgravyboat.vanity.api.condtional.conditions.*;

import java.util.HashMap;
import java.util.Map;

public class Conditions {

    private static final Map<String, MapCodec<? extends Condition>> CONDITIONS = new HashMap<>();

    public static final Codec<Condition> CODEC = Codec.STRING.partialDispatch(
            "type",
            condition -> DataResult.success(condition.id()),
            Conditions::getCodec
    );

    public static void init() {
        register(ModLoadedCondition.ID, ModLoadedCondition.CODEC);
        register(NotCondition.ID, NotCondition.CODEC);
        register(OrCondition.ID, OrCondition.CODEC);
        register(AndCondition.ID, AndCondition.CODEC);
    }

    public static void register(String id, MapCodec<? extends Condition> codec) {
        CONDITIONS.put(id, codec);
    }

    private static DataResult<MapCodec<? extends Condition>> getCodec(String id) {
        return CONDITIONS.containsKey(id) ? DataResult.success(CONDITIONS.get(id)) : DataResult.error(() -> "Unknown condition id: " + id);
    }
}

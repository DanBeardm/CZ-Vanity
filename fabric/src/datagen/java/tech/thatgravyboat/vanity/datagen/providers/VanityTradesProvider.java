package tech.thatgravyboat.vanity.datagen.providers;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import tech.thatgravyboat.vanity.common.Vanity;
import tech.thatgravyboat.vanity.common.handler.trades.TradeStack;
import tech.thatgravyboat.vanity.common.handler.trades.VillagerTrade;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class VanityTradesProvider extends FabricCodecDataProvider<VillagerTrade> {

    private static final String DYES_GROUP = "vanity:dyes";
    private static final List<Item> DYES = List.of(
            Items.WHITE_DYE,
            Items.ORANGE_DYE,
            Items.MAGENTA_DYE,
            Items.LIGHT_BLUE_DYE,
            Items.YELLOW_DYE,
            Items.LIME_DYE,
            Items.PINK_DYE,
            Items.GRAY_DYE,
            Items.LIGHT_GRAY_DYE,
            Items.CYAN_DYE,
            Items.PURPLE_DYE,
            Items.BLUE_DYE,
            Items.BROWN_DYE,
            Items.GREEN_DYE,
            Items.RED_DYE,
            Items.BLACK_DYE
    );

    public VanityTradesProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(dataOutput, registriesFuture, PackOutput.Target.DATA_PACK, "vanity/trades", VillagerTrade.CODEC);
    }

    @Override
    protected void configure(BiConsumer<ResourceLocation, VillagerTrade> adder, HolderLookup.Provider provider) {
        for (Item dye : DYES) {
            String name = BuiltInRegistries.ITEM.getKey(dye).getPath();
            adder.accept(
                    Vanity.id("buy_%s".formatted(name)),
                    new VillagerTrade(
                            1,
                            DYES_GROUP,
                            new TradeStack(dye, UniformInt.of(16, 42)),
                            Optional.empty(),
                            new TradeStack(Items.EMERALD),
                            UniformInt.of(2, 6),
                            ConstantInt.of(5),
                            ConstantFloat.of(0.1f),
                            1f
                    )
            );
            adder.accept(
                    Vanity.id("sell_%s".formatted(name)),
                    new VillagerTrade(
                            2,
                            DYES_GROUP,
                            new TradeStack(Items.EMERALD),
                            Optional.empty(),
                            new TradeStack(dye, UniformInt.of(3, 15)),
                            UniformInt.of(2, 6),
                            ConstantInt.of(10),
                            ConstantFloat.of(0.1f),
                            1f
                    )
            );
        }
    }

    @Override
    public @NotNull String getName() {
        return "Vanity Villager Trades";
    }
}

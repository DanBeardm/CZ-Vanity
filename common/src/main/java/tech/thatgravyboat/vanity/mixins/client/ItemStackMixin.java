package tech.thatgravyboat.vanity.mixins.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tech.thatgravyboat.vanity.common.item.DesignHelper;

import java.util.List;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Inject(
            method = "getTooltipLines",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;add(Ljava/lang/Object;)Z",
                    ordinal = 0,
                    shift = At.Shift.AFTER
            )
    )
    private void vanity$styleName(Item.TooltipContext tooltipContext, Player player, TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> cir, @Local(ordinal = 0) List<Component> lines) {
        var style = DesignHelper.getStyle((ItemStack) (Object) this);
        if (style != null) {
            lines.add(DesignHelper.getTranslationKey(style.getFirst(), style.getSecond()).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        }
    }
}

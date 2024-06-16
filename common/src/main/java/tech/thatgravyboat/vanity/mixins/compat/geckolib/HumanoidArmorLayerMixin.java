package tech.thatgravyboat.vanity.mixins.compat.geckolib;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tech.thatgravyboat.vanity.api.style.AssetTypes;
import tech.thatgravyboat.vanity.client.compat.geckolib.StyledArmorGeoAnimatable;
import tech.thatgravyboat.vanity.client.compat.geckolib.StyledGeoArmorRenderer;
import tech.thatgravyboat.vanity.client.design.ClientDesignManager;

@Mixin(HumanoidArmorLayer.class)
public class HumanoidArmorLayerMixin<T extends LivingEntity, A extends HumanoidModel<T>> {

    @Inject(
            method = "renderArmorPiece",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/layers/HumanoidArmorLayer;usesInnerModel(Lnet/minecraft/world/entity/EquipmentSlot;)Z"
            ),
            cancellable = true
    )
    private void vanity$changeModel(PoseStack stack, MultiBufferSource source, T entity, EquipmentSlot slot, int light, A model, CallbackInfo ci) {
        ItemStack item = entity.getItemBySlot(slot);
        if (ClientDesignManager.INSTANCE.hasAsset(item, AssetTypes.GECKOLIB_ARMOR)) {
            StyledArmorGeoAnimatable animatable = StyledArmorGeoAnimatable.get(item);
            if (animatable != null) {
                HumanoidModel<LivingEntity> original = (HumanoidModel<LivingEntity>) model;
                HumanoidModel<LivingEntity> replacement = animatable.getModel(entity, item, slot, original);
                if (replacement != original && replacement instanceof StyledGeoArmorRenderer renderer) {
                    renderer.prepForRender(entity, item, slot, original);
                    original.copyPropertiesTo(replacement);
                    renderer.renderToBuffer(stack, null, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
                    ci.cancel();
                }
            }
        }
    }
}

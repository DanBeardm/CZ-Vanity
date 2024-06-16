package tech.thatgravyboat.vanity.mixins.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tech.thatgravyboat.vanity.client.VanityClient;

@Mixin(ModelBakery.class)
public abstract class ModelBakeryMixin {

    @Shadow protected abstract void loadSpecialItemModelAndDependencies(ModelResourceLocation modelResourceLocation);

    @Inject(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/resources/model/ModelBakery;loadSpecialItemModelAndDependencies(Lnet/minecraft/client/resources/model/ModelResourceLocation;)V",
                    shift = At.Shift.AFTER,
                    ordinal = 1
            )
    )
    private void onInit(CallbackInfo ci) {
        VanityClient.registerModels(Minecraft.getInstance().getResourceManager(), this::loadSpecialItemModelAndDependencies);
    }
}

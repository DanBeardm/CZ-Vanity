package tech.thatgravyboat.vanity.neoforge;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;
import tech.thatgravyboat.vanity.client.VanityClient;
import tech.thatgravyboat.vanity.common.Vanity;

@EventBusSubscriber(modid = Vanity.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class VanityForgeClient {

    @SubscribeEvent
    public static void onEvent(ModelEvent.RegisterAdditional event) {
        VanityClient.registerModels(Minecraft.getInstance().getResourceManager(), event::register);
    }
}

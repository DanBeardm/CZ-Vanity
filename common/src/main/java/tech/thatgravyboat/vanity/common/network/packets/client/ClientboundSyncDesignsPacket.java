package tech.thatgravyboat.vanity.common.network.packets.client;

import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import com.teamresourceful.resourcefullib.common.bytecodecs.ExtraByteCodecs;
import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.teamresourceful.resourcefullib.common.network.defaults.CodecPacketType;
import net.minecraft.resources.ResourceLocation;
import tech.thatgravyboat.vanity.api.design.Design;
import tech.thatgravyboat.vanity.client.VanityClientNetwork;
import tech.thatgravyboat.vanity.common.Vanity;
import tech.thatgravyboat.vanity.common.handler.design.DesignManagerImpl;

import java.util.Map;

public record ClientboundSyncDesignsPacket(
        Map<ResourceLocation, Design> designs
) implements Packet<ClientboundSyncDesignsPacket> {

    public static final ClientboundPacketType<ClientboundSyncDesignsPacket> TYPE = new Type();

    public ClientboundSyncDesignsPacket(DesignManagerImpl manager) {
        this(manager.getAllDesigns());
    }

    @Override
    public PacketType<ClientboundSyncDesignsPacket> type() {
        return TYPE;
    }

    private static class Type extends CodecPacketType<ClientboundSyncDesignsPacket> implements ClientboundPacketType<ClientboundSyncDesignsPacket> {

        public Type() {
            super(
                    ClientboundSyncDesignsPacket.class,
                    new ResourceLocation(Vanity.MOD_ID, "sync_designs"),
                    ObjectByteCodec.create(
                            ByteCodec.mapOf(ExtraByteCodecs.RESOURCE_LOCATION, Design.BYTE_CODEC).fieldOf(ClientboundSyncDesignsPacket::designs),
                            ClientboundSyncDesignsPacket::new
                    )
            );
        }

        @Override
        public Runnable handle(ClientboundSyncDesignsPacket message) {
            return () -> VanityClientNetwork.handleSyncDesigns(message);
        }
    }
}

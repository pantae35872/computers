package io.github.pantae35872.computers.networking;

import io.github.pantae35872.computers.Main;
import io.github.pantae35872.computers.networking.packet.ComputerBlockPosS2CPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;

import java.util.Objects;

public class ModNetwork {
    private static final SimpleChannel CHANNEL =
            ChannelBuilder.named(new ResourceLocation(Main.MOD_ID, "messages")).simpleChannel();
    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        CHANNEL.messageBuilder(ComputerBlockPosS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ComputerBlockPosS2CPacket::new)
                .encoder(ComputerBlockPosS2CPacket::encode)
                .consumerMainThread(ComputerBlockPosS2CPacket::receive)
                .add();
    }

    public static <MSG> void sendToClient (MSG packet, PacketDistributor.PacketTarget packetTarget) {
        CHANNEL.send(packet, packetTarget);
    }

    public static <MSG> void sendToServer (MSG packet) {
        CHANNEL.send(packet, Objects.requireNonNull(Minecraft.getInstance().getConnection()).getConnection());
    }
}

package io.github.pantae35872.computers.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

public abstract class ModPacket {
    public ModPacket(FriendlyByteBuf buf) {
        decode(buf);
    }

    public ModPacket() {

    }

    public abstract void encode(FriendlyByteBuf buf);
    public abstract void decode(FriendlyByteBuf buf);

    public void receive(CustomPayloadEvent.Context context) {
        context.enqueueWork(() -> receivePacket(context));
    }

    public abstract void receivePacket(CustomPayloadEvent.Context context);
}

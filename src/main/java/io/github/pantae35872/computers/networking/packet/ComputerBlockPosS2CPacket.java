package io.github.pantae35872.computers.networking.packet;

import io.github.pantae35872.computers.networking.ModPacket;
import io.github.pantae35872.computers.networking.PendingPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class ComputerBlockPosS2CPacket extends ModPacket {
    public BlockPos blockPos;
    public ComputerBlockPosS2CPacket(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public ComputerBlockPosS2CPacket(FriendlyByteBuf buf) {
        super(buf);
    }


    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.blockPos);
    }

    @Override
    public void decode(FriendlyByteBuf buf) {
        this.blockPos = buf.readBlockPos();
    }

    @Override
    public void receivePacket(CustomPayloadEvent.Context context) {
        PendingPacket.addS2C(this);
    }
}

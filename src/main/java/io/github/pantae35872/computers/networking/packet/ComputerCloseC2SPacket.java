package io.github.pantae35872.computers.networking.packet;

import io.github.pantae35872.computers.networking.ModPacket;
import io.github.pantae35872.computers.registries.block_entity.custom.ComputerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.network.CustomPayloadEvent;

import java.util.Objects;

public class ComputerCloseC2SPacket extends ModPacket {
    public BlockPos pos;
    public ComputerCloseC2SPacket(BlockPos blockPos) {
        this.pos = blockPos;
    }
    public ComputerCloseC2SPacket(FriendlyByteBuf buf) {
        super(buf);
    }
    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    @Override
    public void decode(FriendlyByteBuf buf) {
        pos = buf.readBlockPos();
    }

    @Override
    public void receivePacket(CustomPayloadEvent.Context context) {
        ServerLevel level = Objects.requireNonNull(context.getSender()).serverLevel();
        if (level.getBlockEntity(pos) instanceof ComputerBlockEntity blockEntity) {
            blockEntity.clientClose();
        }
    }
}

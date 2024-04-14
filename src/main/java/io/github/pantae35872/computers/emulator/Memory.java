package io.github.pantae35872.computers.emulator;

import io.github.pantae35872.computers.emulator.instructions.registers.CoreRegisterTypes;
import net.minecraft.nbt.CompoundTag;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class Memory {

    private final ByteBuffer memory;

    public Memory(int amount) {
        this.memory = ByteBuffer.allocate(amount);
    }

    public Memory(CompoundTag compoundTag) {
        byte[] data = compoundTag.getByteArray("memory");
        ByteBuffer buffer = ByteBuffer.allocate(data.length);
        this.memory = buffer.put(data);
    }

    public byte[] getBytes(Address address, int amount) {
        byte[] value = new byte[amount];
        for (int i = 0; i < amount; i++) {
            value[i] = memory.get(address.getRaw() + i);
        }
        return value;
    }

    public void serialize(CompoundTag compoundTag) {
        compoundTag.putByteArray("memory", memory.array());
    }

    public void setBytes(Address address, byte[] value) {
        memory.put(address.getRaw(), value);
    }

    public ByteBuffer getInstruction(Address address) {
        return ByteBuffer.wrap(getBytes(address, 10));
    }
}

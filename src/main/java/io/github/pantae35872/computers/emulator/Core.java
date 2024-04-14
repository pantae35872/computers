package io.github.pantae35872.computers.emulator;

import io.github.pantae35872.computers.Main;
import io.github.pantae35872.computers.emulator.instructions.compare.CompareResultType;
import io.github.pantae35872.computers.emulator.instructions.registers.CoreRegisterTypes;
import net.minecraft.nbt.CompoundTag;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicReference;

public class Core {
    // instruction pointer;
    private Address ip;
    private int ax = 0;
    private int ae = 0;
    private int ea = 0;
    private CompareResultType compare_result;
    private boolean halt;
    private boolean increase_ip;

    private final AtomicReference<Memory> memory;

    public Core(AtomicReference<Memory> memory) {
        this.memory = memory;
        this.halt = false;
        this.ip = new Address(0);
    }

    public void tick() {
        if (!halt) {
            this.increase_ip = true;
            ByteBuffer byte_instruction = this.memory.get().getInstruction(ip);
            Instructions instruction = Instructions.from_index(byte_instruction.get());
            instruction.getExecutor().parse_args(byte_instruction);
            instruction.getExecutor().execute(this);

            if (increase_ip) {
                ip.inc(Emulator.INSTRUCTION_SIZE);
            }
        }
    }

    public byte[] read_memory(Address address, int amount) {
        return memory.get().getBytes(address, amount);
    }

    public void write_memory(Address address, byte[] value) {
        memory.get().setBytes(address, value);
    }

    public void no_increase() {
        this.increase_ip = false;
    }

    public void set_compare(CompareResultType type) {
        this.compare_result = type;
    }
    public CompareResultType get_compare() {
        return this.compare_result;
    }

    public void setRegisterByType(CoreRegisterTypes type, int value) {
        switch (type) {
            case IP -> {
                this.ip = new Address(value);
            }

            case AE -> {
                this.ae = value;
            }

            case AX -> {
                this.ax = value;
            }

            case EA -> {
                this.ea = value;
            }
        }
    }


    public int getRegisterByType(CoreRegisterTypes type) {
        switch (type) {
            case IP -> {
                return this.ip.getRaw();
            }

            case AE -> {
                return this.ae;
            }

            case AX -> {
                return this.ax;
            }
            case EA -> {
                return this.ea;
            }
            default -> {
                return 0;
            }
        }
    }

    public void halt() {
        this.halt = true;
        Main.LOGGER.debug("hlt reach");
    }

    public void serialize(CompoundTag compoundTag) {
        compoundTag.putInt("ip", ip.getRaw());
        compoundTag.putInt("ax", ax);
        compoundTag.putInt("ae", ae);
        compoundTag.putInt("ea", ea);
        compoundTag.putBoolean("increase_ip", this.increase_ip);
        compoundTag.putBoolean("halt", this.halt);
        if (compare_result != null) compare_result.serialize(compoundTag);
    }

    public void deserialize(CompoundTag compoundTag) {
        this.ip.setAddress(compoundTag.getInt("ip"));
        this.ax = compoundTag.getInt("ax");
        this.ae = compoundTag.getInt("ae");
        this.ea = compoundTag.getInt("ea");
        if (compoundTag.contains("resultType"))
            this.compare_result = CompareResultType.from_nbt(compoundTag);
        this.increase_ip = compoundTag.getBoolean("increase_ip");
        this.halt = compoundTag.getBoolean("halt");
    }

    public void interrupt() {

    }
}

package io.github.pantae35872.computers.emulator;

import net.minecraft.nbt.CompoundTag;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class Emulator {
    // memory
    private Memory memory;
    private final ArrayList<Core> core;
    public static final int INSTRUCTION_SIZE = 10;

    public Emulator(int ram_amount, int core_amount) {
        memory = new Memory(ram_amount);
        core = new ArrayList<>(core_amount);
        for (int i = 0; i < core_amount; i++) {
            core.add(new Core(new AtomicReference<>(memory)));
        }
        memory.setBytes(new Address(0), new Compiler(12)
                .add("main:")
                .add("mov ae, 0")
                .add("mov ax, 1")
                .add("fib:")
                .add("mov ea, 0")
                .add("add ea, ae")
                .add("add ea, ax")
                .add("mov ax, ae")
                .add("mov ae, ea")
                .add("cmp ae, 233")
                .add("je halt")
                .add("jmp fib")
                .add("halt:")
                .add("hlt").compile().array());
    }

    public void deserialize(CompoundTag compoundTag) {
        memory = new Memory(compoundTag);
        this.core.forEach(core -> core.deserialize(compoundTag));
    }

    public void serialize(CompoundTag compoundTag) {
        memory.serialize(compoundTag);
        this.core.forEach(core -> core.serialize(compoundTag));
    }

    public void tick() {
        core.forEach(Core::tick);
    }
}

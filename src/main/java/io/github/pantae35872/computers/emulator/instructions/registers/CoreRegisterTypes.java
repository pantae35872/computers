package io.github.pantae35872.computers.emulator.instructions.registers;

import io.github.pantae35872.computers.emulator.Instructions;
import io.github.pantae35872.computers.emulator.instructions.InstructionExecutor;

import java.util.Objects;

public enum CoreRegisterTypes {
    IP("ip",1, Integer.BYTES),
    AX("ax",2, Integer.BYTES),
    AE("ae",3, Integer.BYTES),
    EA("ea", 4, Integer.BYTES),
    ;
    private final int index;
    private final String name;
    private final int size;

    CoreRegisterTypes(String name, int index, int size) {
        this.name = name;
        this.index = index;
        this.size = size;
    }

    public int getIndex() {
        return this.index;
    }

    public int getSize() {
        return this.size;
    }

    public static CoreRegisterTypes from_index(int index) {
        for (CoreRegisterTypes instruction : values()) {
            if (instruction.index == index) {
                return instruction;
            }
        }
        throw new IllegalArgumentException("No register found from instruction index: "
                + index);
    }

    public static CoreRegisterTypes from_string(String name) {
        for (CoreRegisterTypes instruction : values()) {
            if (Objects.equals(instruction.name, name)) {
                return instruction;
            }
        }
        throw new IllegalArgumentException("No register found from string: "+name);
    }
}

package io.github.pantae35872.computers.emulator;

import io.github.pantae35872.computers.emulator.instructions.*;
import io.github.pantae35872.computers.emulator.instructions.compare.CompareInstruction;
import io.github.pantae35872.computers.emulator.instructions.move.MovInstruction;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum Instructions implements StringRepresentable {
    JUMP("jmp", 1, new JumpInstruction(), true),
    HALT("hlt", 10, new HaltInstruction()),
    ADD("add", 15, new AddInstruction()),
    MOV("mov", 2, new MovInstruction()),
    COMPARE("cmp", 16, new CompareInstruction()),
    JUMP_EQUAL("je", 3, new JumpEqualInstruction(), true),
    JUMP_MORE_THAN("jm", 4, new JumpMoreThanInstruction(), true),
    JUMP_LESS_THAN("jl", 5, new JumpLessThanInstruction(), true),
    INCREMENT("inc", 16, new IncrementInstruction()),
    SUBTRACT("sub", 17, new SubtractInstruction());

    private final int instruction_index;
    private final String name;
    private final InstructionExecutor executor;
    private final boolean isJump;

    Instructions(String name, int instruction_index, InstructionExecutor executor, boolean isJump) {
        this.name = name;
        this.instruction_index = instruction_index;
        this.executor = executor;
        this.isJump = isJump;
    }


    Instructions(String name, int instruction_index, InstructionExecutor executor) {
        this(name, instruction_index, executor, false);
    }

    public boolean isJump() {
        return this.isJump;
    }

    public int getInstructionIndex() {
        return instruction_index;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }

    public InstructionExecutor getExecutor() {
        return this.executor;
    }

    public static Instructions from_index(int instruction_index) {
        for (Instructions instruction : values()) {
            if (instruction.getInstructionIndex() == instruction_index) {
                return instruction;
            }
        }
        throw new IllegalArgumentException("No instruction found from instruction index: "
                + instruction_index);
    }

    public static Instructions from_string(String instruction_name) {
        for (Instructions instruction : values()) {
            if (instruction.getSerializedName().equals(instruction_name)) {
                return instruction;
            }
        }
        throw new IllegalArgumentException("No instruction found from instruction index: "
                + instruction_name);
    }
}

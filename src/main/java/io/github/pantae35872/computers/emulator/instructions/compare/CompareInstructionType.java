package io.github.pantae35872.computers.emulator.instructions.compare;

import io.github.pantae35872.computers.emulator.instructions.move.MoveInstructionType;

public enum CompareInstructionType {
    NUMBER_TO_REGISTER((byte)1),
    REGISTER_TO_REGISTER((byte)4);

    public final byte index;

    CompareInstructionType(byte index) {
        this.index = index;
    }

    public static CompareInstructionType from_byte(byte index) {
        for (CompareInstructionType type : values()) {
            if (type.index == index) {
                return type;
            }
        }

        throw new IllegalArgumentException("No register found from byte: "+index);
    }

    public byte to_byte() {
        return this.index;
    }

}

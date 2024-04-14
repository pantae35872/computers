package io.github.pantae35872.computers.emulator.instructions.move;

public enum MoveInstructionType {
    NUMBER_TO_REGISTER((byte)1),
    REGISTER_TO_ADDRESS((byte)2),
    ADDRESS_TO_REGISTER((byte)3),
    REGISTER_TO_REGISTER((byte)4);

    public final byte index;

    MoveInstructionType(byte index) {
        this.index = index;
    }

    public static MoveInstructionType from_byte(byte index) {
        for (MoveInstructionType type : values()) {
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

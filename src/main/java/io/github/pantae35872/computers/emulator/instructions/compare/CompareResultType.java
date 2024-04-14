package io.github.pantae35872.computers.emulator.instructions.compare;

import net.minecraft.nbt.CompoundTag;

public enum CompareResultType {
    EQUAL(1),
    LESS_THAN(2),
    MORE_THAN(3);

    private final int index;

    CompareResultType(int index) {
        this.index = index;
    }

    public static CompareResultType from_nbt(CompoundTag tag) {
        int value = tag.getInt("resultType");
        for (CompareResultType type : values()) {
            if (type.index == value) {
                return type;
            }
        }

        throw new IllegalArgumentException("could not find compare result type from compound tag: "
                + tag);
    }

    public void serialize(CompoundTag compoundTag) {
        compoundTag.putInt("resultType", this.index);
    }
}

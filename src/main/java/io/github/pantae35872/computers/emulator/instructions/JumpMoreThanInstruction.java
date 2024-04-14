package io.github.pantae35872.computers.emulator.instructions;

import io.github.pantae35872.computers.emulator.ArgumentParser;
import io.github.pantae35872.computers.emulator.Core;
import io.github.pantae35872.computers.emulator.instructions.compare.CompareResultType;

import java.nio.ByteBuffer;

public class JumpMoreThanInstruction extends AbstractJumpInstruction {
    @Override
    public void execute(Core core) {
        if (core.get_compare() == CompareResultType.MORE_THAN) {
            super.execute(core);
        }
    }
}

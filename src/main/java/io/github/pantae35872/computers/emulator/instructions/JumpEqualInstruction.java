package io.github.pantae35872.computers.emulator.instructions;

import io.github.pantae35872.computers.emulator.Address;
import io.github.pantae35872.computers.emulator.ArgumentParser;
import io.github.pantae35872.computers.emulator.Core;
import io.github.pantae35872.computers.emulator.instructions.compare.CompareResultType;
import io.github.pantae35872.computers.emulator.instructions.registers.CoreRegisterTypes;

import java.nio.ByteBuffer;
import java.util.Optional;

public class JumpEqualInstruction extends AbstractJumpInstruction {
    @Override
    public void execute(Core core) {
        if (core.get_compare() == CompareResultType.EQUAL) {
            super.execute(core);
        }
    }
}

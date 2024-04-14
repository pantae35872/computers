package io.github.pantae35872.computers.emulator.instructions;

import io.github.pantae35872.computers.emulator.Core;
import io.github.pantae35872.computers.emulator.instructions.compare.CompareResultType;

public class JumpLessThanInstruction extends AbstractJumpInstruction{
    @Override
    public void execute(Core core) {
        if (core.get_compare() == CompareResultType.LESS_THAN) {
            super.execute(core);
        }
    }
}

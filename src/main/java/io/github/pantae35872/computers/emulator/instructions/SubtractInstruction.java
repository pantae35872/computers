package io.github.pantae35872.computers.emulator.instructions;

import io.github.pantae35872.computers.emulator.ArgumentParser;
import io.github.pantae35872.computers.emulator.Core;
import io.github.pantae35872.computers.emulator.instructions.registers.CoreRegisterTypes;

import java.nio.ByteBuffer;
import java.util.Optional;

public class SubtractInstruction extends InstructionExecutor {
    public Optional<CoreRegisterTypes> arg1 = Optional.empty();
    public Optional<CoreRegisterTypes> arg2 = Optional.empty();
    @Override
    public void execute(Core core) {
        if (arg1.isPresent() && arg2.isPresent()) {
            int val1 = core.getRegisterByType(arg1.get());
            int val2 = core.getRegisterByType(arg2.get());
            core.setRegisterByType(arg1.get(), val1 - val2);
        } else {
            throw new IllegalArgumentException("Could not execute add instruction unparse argument");
        }
    }

    @Override
    public void parse_args(ByteBuffer args) {
        this.arg1 = Optional.of(CoreRegisterTypes.from_index(args.getInt()));
        this.arg2 = Optional.of(CoreRegisterTypes.from_index(args.getInt()));
    }

    @Override
    public void parse_args_from_string(ByteBuffer buffer, ArgumentParser args) {
        buffer.putInt(args.getRegister(0).getIndex());
        buffer.putInt(args.getRegister(1).getIndex());
    }
}

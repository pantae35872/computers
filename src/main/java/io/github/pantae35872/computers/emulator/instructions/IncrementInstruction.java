package io.github.pantae35872.computers.emulator.instructions;

import io.github.pantae35872.computers.emulator.Address;
import io.github.pantae35872.computers.emulator.ArgumentParser;
import io.github.pantae35872.computers.emulator.Core;
import io.github.pantae35872.computers.emulator.instructions.registers.CoreRegisterTypes;

import java.nio.ByteBuffer;
import java.util.Optional;

public class IncrementInstruction extends InstructionExecutor{
    public Optional<CoreRegisterTypes> arg1 = Optional.empty();
    @Override
    public void execute(Core core) {
        arg1.ifPresent(coreRegisterTypes ->
                core.setRegisterByType(coreRegisterTypes, core.getRegisterByType(coreRegisterTypes) + 1));
    }

    @Override
    public void parse_args(ByteBuffer args) {
        this.arg1 = Optional.of(CoreRegisterTypes.from_index(args.getInt()));
    }

    @Override
    public void parse_args_from_string(ByteBuffer buffer, ArgumentParser args) {
        buffer.putInt(args.getRegister(0).getIndex());
    }
}

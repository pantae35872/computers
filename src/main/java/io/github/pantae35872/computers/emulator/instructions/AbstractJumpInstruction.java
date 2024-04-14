package io.github.pantae35872.computers.emulator.instructions;

import io.github.pantae35872.computers.emulator.Address;
import io.github.pantae35872.computers.emulator.ArgumentParser;
import io.github.pantae35872.computers.emulator.Core;
import io.github.pantae35872.computers.emulator.instructions.registers.CoreRegisterTypes;

import java.nio.ByteBuffer;
import java.util.Optional;

public abstract class AbstractJumpInstruction extends InstructionExecutor{
    public Optional<Address> arg1 = Optional.empty();
    @Override
    public void execute(Core core) {
        core.no_increase();
        arg1.ifPresent((arg) -> core.setRegisterByType(CoreRegisterTypes.IP,
                arg.getRaw()));
    }

    @Override
    public void parse_args(ByteBuffer args) {
        arg1 = Optional.of(new Address(args.getInt()));
    }

    @Override
    public void parse_args_from_string(ByteBuffer buffer, ArgumentParser args) {
        buffer.putInt(args.getAddress(0).getRaw());
    }
}

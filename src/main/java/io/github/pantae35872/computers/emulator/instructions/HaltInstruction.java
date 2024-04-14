package io.github.pantae35872.computers.emulator.instructions;

import io.github.pantae35872.computers.emulator.ArgumentParser;
import io.github.pantae35872.computers.emulator.Core;
import io.github.pantae35872.computers.emulator.Emulator;

import java.nio.ByteBuffer;
import java.util.Optional;

public class HaltInstruction extends InstructionExecutor{
    @Override
    public void execute(Core core) {
        core.halt();
    }

    @Override
    public void parse_args(ByteBuffer args) {

    }

    @Override
    public void parse_args_from_string(ByteBuffer buffer, ArgumentParser args) {
    }
}

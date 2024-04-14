package io.github.pantae35872.computers.emulator.instructions;

import io.github.pantae35872.computers.emulator.ArgumentParser;
import io.github.pantae35872.computers.emulator.Core;
import io.github.pantae35872.computers.emulator.Emulator;

import java.nio.ByteBuffer;

public class NopInstruction extends InstructionExecutor{
    @Override
    public void execute(Core core) {
    }

    @Override
    public void parse_args(ByteBuffer args) {
    }

    @Override
    public void parse_args_from_string(ByteBuffer buffer, ArgumentParser args) {
    }
}

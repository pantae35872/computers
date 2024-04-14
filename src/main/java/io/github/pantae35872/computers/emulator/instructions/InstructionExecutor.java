package io.github.pantae35872.computers.emulator.instructions;

import io.github.pantae35872.computers.emulator.ArgumentParser;
import io.github.pantae35872.computers.emulator.Core;

import java.nio.ByteBuffer;
import java.util.Optional;

public abstract class InstructionExecutor {
    public abstract void execute(Core core);
    public abstract void parse_args(ByteBuffer args);
    public abstract void parse_args_from_string(ByteBuffer buffer, ArgumentParser args);
}

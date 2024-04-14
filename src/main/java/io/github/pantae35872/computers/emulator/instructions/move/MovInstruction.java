package io.github.pantae35872.computers.emulator.instructions.move;

import io.github.pantae35872.computers.emulator.Address;
import io.github.pantae35872.computers.emulator.ArgumentParser;
import io.github.pantae35872.computers.emulator.Core;
import io.github.pantae35872.computers.emulator.instructions.InstructionExecutor;
import io.github.pantae35872.computers.emulator.instructions.registers.CoreRegisterTypes;
import io.github.pantae35872.computers.utils.ByteUtils;

import java.nio.ByteBuffer;
import java.util.Optional;

public class MovInstruction extends InstructionExecutor {
    private Optional<Integer> arg1 = Optional.empty();
    private Optional<Integer> arg2 = Optional.empty();

    private MoveInstructionType type;

    @Override
    public void execute(Core core) {
        if (arg1.isPresent() && arg2.isPresent()) {
            switch (type) {
                case NUMBER_TO_REGISTER -> core.setRegisterByType(CoreRegisterTypes.from_index(
                        arg1.get()), arg2.get());
                case REGISTER_TO_REGISTER -> core.setRegisterByType(CoreRegisterTypes.from_index(
                        arg1.get()
                ),
                        core.getRegisterByType(CoreRegisterTypes.from_index(arg2.get())));
                case ADDRESS_TO_REGISTER -> {
                    CoreRegisterTypes type = CoreRegisterTypes.from_index(
                            arg1.get()
                    );
                    core.setRegisterByType(type, ByteUtils.intFromBytes(
                            core.read_memory(new Address(arg2.get()),type.getSize())));
                }
                case REGISTER_TO_ADDRESS -> {
                    CoreRegisterTypes type = CoreRegisterTypes.from_index(
                            arg2.get()
                    );
                    ByteBuffer byte_value = ByteBuffer.allocate(4);
                    byte_value.putInt(core.getRegisterByType(type));

                    core.write_memory(new Address(arg1.get()),
                            byte_value.array());
                }
                default -> {}
            }
        } else {
            throw new IllegalArgumentException("Could not execute mov instruction missing argument");
        }
    }

    @Override
    public void parse_args(ByteBuffer args) {
        type = MoveInstructionType.from_byte(args.get());

        arg1 = Optional.of(args.getInt());

        arg2 = Optional.of(args.getInt());
    }

    @Override
    public void parse_args_from_string(ByteBuffer buffer, ArgumentParser args) {
        try {
            CoreRegisterTypes arg1 = args.getRegister(0);
            CoreRegisterTypes arg2 = args.getRegister(1);
            buffer.put(MoveInstructionType.REGISTER_TO_REGISTER.to_byte());
            buffer.putInt(arg1.getIndex());
            buffer.putInt(arg2.getIndex());
            return;
        } catch (Exception ignored) {
        }

        try {
            CoreRegisterTypes arg1 = args.getRegister(0);
            int arg2 = args.getInt(1);
            buffer.put(MoveInstructionType.NUMBER_TO_REGISTER.to_byte());
            buffer.putInt(arg1.getIndex());
            buffer.putInt(arg2);
        } catch (Exception ignored) {

        }
    }
}

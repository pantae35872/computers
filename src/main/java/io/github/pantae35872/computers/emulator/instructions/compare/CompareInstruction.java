package io.github.pantae35872.computers.emulator.instructions.compare;

import io.github.pantae35872.computers.emulator.ArgumentParser;
import io.github.pantae35872.computers.emulator.Core;
import io.github.pantae35872.computers.emulator.instructions.InstructionExecutor;
import io.github.pantae35872.computers.emulator.instructions.move.MoveInstructionType;
import io.github.pantae35872.computers.emulator.instructions.registers.CoreRegisterTypes;

import java.nio.ByteBuffer;
import java.util.Optional;

public class CompareInstruction extends InstructionExecutor {
    private Optional<Integer> arg1 = Optional.empty();
    private Optional<Integer> arg2 = Optional.empty();

    private CompareInstructionType type;
    @Override
    public void execute(Core core) {
        if (arg1.isPresent() && arg2.isPresent()) {
            switch (type) {
                case REGISTER_TO_REGISTER -> {
                    int arg1 = core.getRegisterByType(CoreRegisterTypes.from_index(this.arg1.get()));
                    int arg2 = core.getRegisterByType(CoreRegisterTypes.from_index(this.arg2.get()));

                    if (arg1 == arg2) {
                        core.set_compare(CompareResultType.EQUAL);
                    } else if (arg1 < arg2){
                        core.set_compare(CompareResultType.LESS_THAN);
                    } else {
                        core.set_compare(CompareResultType.MORE_THAN);
                    }
                }
                case NUMBER_TO_REGISTER -> {
                    int arg1 = core.getRegisterByType(CoreRegisterTypes.from_index(this.arg1.get()));
                    int arg2 = this.arg2.get();

                    if (arg1 == arg2) {
                        core.set_compare(CompareResultType.EQUAL);
                    } else if (arg1 < arg2){
                        core.set_compare(CompareResultType.LESS_THAN);
                    } else {
                        core.set_compare(CompareResultType.MORE_THAN);
                    }
                }
                default -> {}
            }
        }
        else {
            throw new IllegalArgumentException("Could not execute cmp instruction missing argument");
        }
    }

    @Override
    public void parse_args(ByteBuffer args) {
        type = CompareInstructionType.from_byte(args.get());

        arg1 = Optional.of(args.getInt());

        arg2 = Optional.of(args.getInt());
    }

    @Override
    public void parse_args_from_string(ByteBuffer buffer, ArgumentParser args) {
        try {
            CoreRegisterTypes arg1 = args.getRegister(0);
            CoreRegisterTypes arg2 = args.getRegister(1);
            buffer.put(CompareInstructionType.REGISTER_TO_REGISTER.to_byte());
            buffer.putInt(arg1.getIndex());
            buffer.putInt(arg2.getIndex());
            return;
        } catch (Exception ignored) {
        }

        try {
            CoreRegisterTypes arg1 = args.getRegister(0);
            int arg2 = args.getInt(1);
            buffer.put(CompareInstructionType.NUMBER_TO_REGISTER.to_byte());
            buffer.putInt(arg1.getIndex());
            buffer.putInt(arg2);
        } catch (Exception ignored) {

        }
    }
}

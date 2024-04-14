package io.github.pantae35872.computers.emulator;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Compiler {

    private final Map<String, Integer> headers;
    private final List<String> instructions;
    private final ByteBuffer buffer;
    private int current_pos = 0;

    public Compiler(int length) {
        buffer = ByteBuffer.allocate(length * 10);
        headers = new HashMap<>();
        instructions = new ArrayList<>();
    }

    public Compiler add(String instruction) {
        if (instruction.endsWith(":")) {
            headers.put(instruction.substring(0, instruction.length() - 1), current_pos);
        } else {
            this.instructions.add(instruction);
            current_pos += 10;
        }
        return this;
    }

    private String getInstruction(String value) {
        String val = "";
        for (char ch : value.toCharArray()) {
            if (ch == ' ') {
                break;
            }

            val = val.concat(String.valueOf(ch));
        }
        return val;
    }

    public ByteBuffer compile() {
        instructions.forEach((instruction) -> {
            String extracted_instruction = getInstruction(instruction);
            Instructions instructions = Instructions.from_string(extracted_instruction);
            String args = instruction.substring(extracted_instruction.length());
            if (instructions.isJump()) {
                args = args.replaceAll("\\s", "");
                args = args.replace(args, String.valueOf(headers.get(args)));
            }
            buffer.put((byte) instructions.getInstructionIndex());
            instructions.getExecutor().parse_args_from_string(buffer,
                    new ArgumentParser(args));
            int left_over = Emulator.INSTRUCTION_SIZE - (buffer.position() % Emulator.INSTRUCTION_SIZE);
            if (left_over != 10) {
                for (int i = 0; i < left_over; i++) {
                    buffer.put((byte) 0);
                }
            }
        });
        return buffer;
    }
}

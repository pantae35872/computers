package io.github.pantae35872.computers.emulator;

import io.github.pantae35872.computers.emulator.instructions.registers.CoreRegisterTypes;

import java.util.ArrayList;
import java.util.List;

public class ArgumentParser {
    private final List<String> args;

    public ArgumentParser(String argument) {
        this.args = new ArrayList<>();
        String args = argument.replaceAll("\\s", "");

        String arg = "";
        for (char charactor : args.toCharArray()) {
            if (charactor == ',') {
                this.args.add(arg);
                arg = "";
            } else {
                arg = arg.concat(String.valueOf(charactor));
            }
        }
        this.args.add(arg);
    }

    public Address getAddress(int index) {
        return new Address(Integer.parseInt(args.get(index)));
    }

    public int getInt(int index) {
        return Integer.parseInt(args.get(index));
    }

    public CoreRegisterTypes getRegister(int index) {
        return CoreRegisterTypes.from_string(args.get(index));
    }
}

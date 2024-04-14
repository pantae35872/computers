package io.github.pantae35872.computers.emulator;

public class Address {
    private int address;

    public Address(int raw_address) {
        this.address = raw_address;
    }

    public int getRaw() {
        return this.address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public void inc(int amount) {
        this.address += amount;
    }
}

package io.github.pantae35872.computers.utils;

public class ByteUtils {

    public static int intFromBytes(byte[] bytes) {
        int value = 0;
        for (byte b : bytes) {
            value = (value << 8) + (b & 0xFF);
        }
        return value;
    }
}

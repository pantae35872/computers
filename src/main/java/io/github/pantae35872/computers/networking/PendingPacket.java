package io.github.pantae35872.computers.networking;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class PendingPacket {
    public static List<? super ModPacket> PendingS2C = new ArrayList<>();

    public static<O extends ModPacket> void addS2C(O value) {
        PendingS2C.add(value);
    }

    @SuppressWarnings("unchecked")
    public static <E extends ModPacket> E getMenuS2C() {
        return (E) PendingS2C.get(PendingS2C.size() - 1);
    }
}

package agl.impl.utils;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomPicker {
    public static <T> T pick(List<T> list) {
        return list.get(ThreadLocalRandom.current().nextInt(list.size()));
    }
}

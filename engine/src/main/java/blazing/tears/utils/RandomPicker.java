package blazing.tears.utils;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomPicker {
    public static <T> T pick(List<T> list) {
        return list.get(ThreadLocalRandom.current().nextInt(list.size()));
    }

    public static <T> T extract(List<T> list) {
        T obj = list.get(ThreadLocalRandom.current().nextInt(list.size()));
        list.remove(obj);
        return obj;
    }
}

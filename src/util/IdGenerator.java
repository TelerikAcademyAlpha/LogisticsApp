package util;

import java.util.concurrent.atomic.AtomicInteger;

public class IdGenerator {
    AtomicInteger id = new AtomicInteger();

    public void incrementId(){
        id.incrementAndGet();
    }
}

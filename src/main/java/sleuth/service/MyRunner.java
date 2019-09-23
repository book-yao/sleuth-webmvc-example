package sleuth.service;

import org.springframework.cloud.sleuth.SpanName;

@SpanName("runner")
public class MyRunner implements Runnable {
    @Override
    public void run() {
        System.out.println("1234");
    }
}

package sleuth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.Calendar;

public class ExecutorTestService {
    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @NewSpan
    public void test(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND,30);
        System.out.println("触发时间："+calendar.getTime());
        threadPoolTaskScheduler.schedule(new MyRunner(), calendar.getTime());
    }

}

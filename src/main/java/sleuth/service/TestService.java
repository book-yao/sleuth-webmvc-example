package sleuth.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.sleuth.SpanName;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.scheduling.annotation.Scheduled;

@SpanName("testService11")
public class TestService {
    Log log = LogFactory.getLog(TestService.class);

    @Scheduled(cron = "0/30 * * * * *")
    @NewSpan
    public String test(){
        System.out.println("1111");
        if(true){
            log.error("错误");
            throw new NullPointerException("发生错误了");
        }
        return "Hello World";
    }
}

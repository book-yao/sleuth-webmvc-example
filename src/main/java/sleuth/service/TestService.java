package sleuth.service;

import brave.Span;
import brave.Tracer;
import brave.Tracing;
import brave.propagation.ExtraFieldPropagation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.SpanName;
import org.springframework.cloud.sleuth.annotation.ContinueSpan;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.cloud.sleuth.annotation.SpanTag;
import org.springframework.scheduling.annotation.Scheduled;

@SpanName("testService11")
public class TestService {
    Log log = LogFactory.getLog(TestService.class);
    @Autowired
    Tracer tracer;

    @Scheduled(cron = "0/10 * * * * *")
    @NewSpan(name = "a-span")
    @SpanName("span-name-test")
    public String test(){
        System.out.println("11===================================================================11");
        Span initialSpan = this.tracer.nextSpan().name("span").start();
        ExtraFieldPropagation.set(initialSpan.context(), "foo", "bar");
        ExtraFieldPropagation.set(initialSpan.context(), "UPPER_CASE", "someValue");
        Span span = tracer.currentSpan();
        span.name("testSpan");
        ExtraFieldPropagation.set(span.context(),"aa","as");
        span.tag("foo","asdf");
        Tracing current = Tracing.current();
//        current.tracer().nextSpan()
//        if(true){
//            log.error("错误");
//            throw new NullPointerException("发生错误了");
//        }
        this.call("13468466");
        return "Hello World";
    }

    @ContinueSpan(log = "second span")
    public void call(@SpanTag(value = "s",expression = "ssas") String s){
        System.out.println("======================="+s);
    }
}

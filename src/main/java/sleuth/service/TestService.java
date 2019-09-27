package sleuth.service;

import brave.Span;
import brave.Tracer;
import brave.Tracing;
import brave.propagation.ExtraFieldPropagation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.MDC;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.SpanName;
import org.springframework.cloud.sleuth.annotation.ContinueSpan;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.cloud.sleuth.annotation.SpanTag;
import org.springframework.cloud.sleuth.annotation.TagValueResolver;
import org.springframework.scheduling.annotation.Scheduled;
import sleuth.inter.CreateSpan;

import javax.validation.constraints.Null;

@SpanName("testService11")
public class TestService {
    Log log = LogFactory.getLog(TestService.class);
    @Autowired
    Tracer tracer;

    @Scheduled(cron = "0/10 * * * * *")
    @NewSpan(name = "a-span")
    @SpanName("span-name-test")
    public String test() {
        System.out.println("11===================================================================11");
        Span initialSpan = this.tracer.nextSpan().name("span").start();
        ExtraFieldPropagation.set(initialSpan.context(), "foo", "bar");
        ExtraFieldPropagation.set(initialSpan.context(), "UPPER_CASE", "someValue");
        Span span = tracer.currentSpan();
        span.name("testSpan");
        log.info("a==========================================");
        ExtraFieldPropagation.set(span.context(), "aa", "as");
        span.tag("foo", "asdf");
        Tracing current = Tracing.current();
//        current.tracer().nextSpan()
//        if(true){
//            log.error("错误");
//            throw new NullPointerException("发生错误了");
//        }
        this.call("13468466");

        return "Hello World";
    }

    //    @ContinueSpan(log = "second span")
    @NewSpan(value = "thread span")
    public void call(@SpanTag(value = "s", expression = "ssas", resolver = TagValueResolver.class) String s) {

        Span tag = tracer.nextSpan().tag("next", "asadas213424");
        tag.name("另一个span");
        tag.start();
        ExtraFieldPropagation.set(tag.context(),"a","as");

        try {

            if (true) {
                throw new NullPointerException("新的span错误了");
            }
            System.out.println("=======================" + s);
        } catch (Exception e) {
            tag.error(e);
        } finally {
            tag.finish();
        }

    }

    @CreateSpan(name = "测试创建一个新的span", success = "测试成功", fail = "测试失败", create = true)
    @NewSpan("测试的一个span 哈哈")
    public void testCreateSpan() {
        System.out.println("MDC=====================================>"+MDC.get("aaa"));
        MDC.put("tag","报错了");
        System.out.println("0909090909090909000090");
//        TestService testService = AopContext.currentProxy() != null ? (TestService) AopContext.currentProxy();
//        (AopContext.currentProxy() != null ? (TestService)AopContext.currentProxy() : this).testCreateSpans("第二个");
        testCreateSpans("第二个");
    }

//    @CreateSpan(name = "测试创建子span", success = "测试成功", fail = "测试失败", create = true)
//    @ContinueSpan(log = "第二个span")
    @NewSpan("第二个span")
    public void testCreateSpans(@SpanTag("s") String s){
        MDC.put("tag","423432");
        System.out.println(s);
    }
}

package sleuth.aop;

import brave.Span;
import brave.Tracer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import sleuth.inter.CreateSpan;

import java.util.Arrays;

@Aspect
@Component
public class SpanAspect {
    @Autowired
    private Tracer tracer;

    @Pointcut(value = "@annotation(sleuth.inter.CreateSpan)")
    public void pointcut(){}


//    @Around("pointcut()")
    public void around(ProceedingJoinPoint pjp){
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        CreateSpan createSpanAnn = signature.getMethod().getAnnotation(CreateSpan.class);
        Span span = this.tracer.currentSpan();
        if(span == null){
            span = this.tracer.newTrace();
        }
//        span = this.tracer.joinSpan(span.context());
        if(createSpanAnn != null){
            if(createSpanAnn.create()){
                span = this.tracer.nextSpan();
                if(!StringUtils.isEmpty(createSpanAnn.name())){
                    span.name(createSpanAnn.name());
                }
                span.start();
            }
        }
        try {
            span.tag("args", Arrays.toString(pjp.getArgs()));
            pjp.proceed(pjp.getArgs());
            if(createSpanAnn != null){
                span.tag("success",createSpanAnn.success());
            }
            span.tag("tag", MDC.get("tag"));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            if(createSpanAnn != null){
                if(!StringUtils.isEmpty(createSpanAnn.fail())){
                    span.error(new Throwable(createSpanAnn.fail()));
                }else{
                    span.error(throwable);
                }
            }
        }finally {
            MDC.clear();
            if(createSpanAnn.create()){
                span.finish();
            }
        }
    }
}

package sleuth.webmvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import sleuth.service.ExecutorTestService;
import sleuth.service.TestService;

@EnableAutoConfiguration
@EnableScheduling
@RestController
@CrossOrigin // So that javascript can be hosted elsewhere
public class Frontend {

  @Autowired RestTemplate restTemplate;
  @Autowired
  private TestService testService;
  @Autowired private ExecutorTestService executorTestService;

  String backendBaseUrl = System.getProperty("spring.example.backendBaseUrl", "http://localhost:9000");

  @RequestMapping("/") public String callBackend() {
    return restTemplate.getForObject(backendBaseUrl + "/api", String.class);
  }

  @PostMapping("/exe")
  public String exe(){
    executorTestService.test();
    return "success";
  }

  @PostMapping("/test") public String callBackend1() {
    String res = null;
    try{
      res = testService.test();
    }catch (Exception e){
      e.printStackTrace();
    }
    return res;
  }

  @Bean RestTemplate restTemplate() {
    return new RestTemplate();
  }
  @Bean TestService testService(){
    return new TestService();
  }

  @Bean
  ExecutorTestService executorTestService(){
    return new ExecutorTestService();
  }


  @Bean
  public ThreadPoolTaskScheduler getThreadPoolTaskScheduler(){
    ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
    threadPoolTaskScheduler.setPoolSize(100);
    threadPoolTaskScheduler.setThreadNamePrefix("self-adaptive-scheduler-thread-pool-");
    //用来设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
    threadPoolTaskScheduler.setWaitForTasksToCompleteOnShutdown(true);
    //设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住
    threadPoolTaskScheduler.setAwaitTerminationSeconds(60);
    threadPoolTaskScheduler.initialize();
    return threadPoolTaskScheduler;
  }

  public static void main(String[] args) {
    SpringApplication.run(Frontend.class,
        "--spring.application.name=frontend",
        "--server.port=8081"
    );
  }
}

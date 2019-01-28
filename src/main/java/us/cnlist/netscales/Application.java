package us.cnlist.netscales;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import sun.misc.Signal;
import sun.misc.SignalHandler;
import us.cnlist.netscales.components.Ipc;

import java.util.concurrent.Executor;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class Application {



    public static void main(String... args) {

        SpringApplication.run(Application.class);


    }

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(84);
        executor.setMaxPoolSize(100);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("data-emitter-");
        executor.initialize();
        return executor;
    }
}

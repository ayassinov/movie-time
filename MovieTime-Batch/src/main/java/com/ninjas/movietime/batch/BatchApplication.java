package com.ninjas.movietime.batch;

import com.ninjas.movietime.batch.cli.ArgumentParser;
import com.ninjas.movietime.batch.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author ayassinov on 06/07/15.
 */
@Slf4j
@Configuration
@ComponentScan
@EnableAutoConfiguration
public class BatchApplication implements CommandLineRunner {


    private TaskService taskService;

    @Autowired
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    public BatchApplication() {
    }


    @Override
    public void run(String... strings) throws Exception {
        taskService.runTask(ArgumentParser.instance().parseTaskArgument(strings));
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    public static void main(String[] args) throws Exception {
        log.info("MovieTimeBatch application is starting...");
        final SpringApplication application = new SpringApplication(BatchApplication.class);
        application.setWebEnvironment(false);

        //run
        final ConfigurableApplicationContext applicationContext = application.run(args);
        //shutdown
        SpringApplication.exit(applicationContext);
    }
}

package com.ninjas.movietime.batch.service;

import com.ninjas.movietime.core.domain.api.TaskEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ayassinov on 11/07/15.
 */
@Slf4j
@Service
public class BatchServiceFacade {

    private final TaskService taskService;

    private final StatusService statusService;

    private final IntegrationService integrationService;


    @Autowired
    public BatchServiceFacade(TaskService taskService, StatusService statusService, IntegrationService integrationService) {
        this.taskService = taskService;
        this.statusService = statusService;
        this.integrationService = integrationService;
    }


    public void run(TaskEnum taskName) {
        taskService.runTask(taskName);
        log.info("Calling run task with arguments {}", taskName);
    }
}

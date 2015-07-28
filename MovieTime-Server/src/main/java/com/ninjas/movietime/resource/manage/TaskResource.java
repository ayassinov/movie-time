package com.ninjas.movietime.resource.manage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Optional;
import com.ninjas.movietime.core.domain.api.Task;
import com.ninjas.movietime.core.util.MetricManager;
import com.ninjas.movietime.service.manage.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ayassinov on 05/09/2014.
 */
@RestController
@RequestMapping("/manage/task")
public class TaskResource {

    private final TaskService taskService;

    @Autowired
    public TaskResource(TaskService taskService) {
        this.taskService = taskService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Task> listTasks() throws JsonProcessingException {
        MetricManager.markResourceMeter("manage", "task", "all");
        return taskService.all();
    }

    @RequestMapping(value = "/{task}", method = RequestMethod.GET)
    public Optional<Task> getByName(@PathVariable("task") String taskName) {
        MetricManager.markResourceMeter("manage", "task", taskName);
        return taskService.getByName(taskName);
    }

    @RequestMapping(value = "/{task}/run", method = RequestMethod.GET)
    public void runTask(@PathVariable("task") String taskName) {
        MetricManager.markResourceMeter("manage", "task", taskName, "run");
        taskService.run(taskName);
    }
}

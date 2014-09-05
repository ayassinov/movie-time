package com.ninjas.movietime.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ninjas.movietime.MovieTimeConfig;
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
public class ScheduledTaskResource {

    private final ObjectMapper mapper;

    private final MovieTimeConfig config;


    @Autowired
    public ScheduledTaskResource(ObjectMapper mapper, MovieTimeConfig config) {
        this.mapper = mapper;
        this.config = config;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String taskNames() throws JsonProcessingException {
        final List<String> tasks = config.getApp().getCron().getNames();
        final ObjectNode nodes = mapper.createObjectNode().putPOJO("tasks", tasks);
        return nodes.toString();
    }

    @RequestMapping(value = "/{task}", method = RequestMethod.GET)
    public String lastExecution(@PathVariable("task") String task) {
        final List<String> tasks = config.getApp().getCron().getNames();
        if (tasks.contains(task)) {
            //select last execution
            return "";
        }
        final ObjectNode nodes = mapper.createObjectNode().put("name", task).put("status", "not found");
        return nodes.toString();
    }
}

package com.ninjas.movietime.batch.repository;

import com.google.common.base.Preconditions;
import com.ninjas.movietime.core.domain.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ayassinov on 07/09/14.
 */
@Repository
public class TaskRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public TaskRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Task save(final Task task) {
        this.mongoTemplate.save(task);
        return task;
    }

    public Task findById(final String taskName) {
        Preconditions.checkNotNull(taskName, "TaskName cannot be null");
        return this.mongoTemplate.findById(taskName.toUpperCase(), Task.class);
    }

    public List<Task> findAll() {
        return this.mongoTemplate.findAll(Task.class);
    }

}

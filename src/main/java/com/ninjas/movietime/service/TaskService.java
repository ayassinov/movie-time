package com.ninjas.movietime.service;

import com.google.common.base.Preconditions;
import com.ninjas.movietime.core.domain.api.Task;
import com.ninjas.movietime.core.util.DateUtils;
import com.ninjas.movietime.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ayassinov on 07/09/14.
 */
@Slf4j
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final IntegrationService integrationService;

    @Autowired
    public TaskService(TaskRepository taskRepository, IntegrationService integrationService) {
        this.taskRepository = taskRepository;
        this.integrationService = integrationService;
    }

    public Task save(Task task) {
        return taskRepository.save(task);
    }

    public List<Task> all() {
        return taskRepository.findAll();
    }

    public Task getByName(String taskName) {
        return taskRepository.findById(taskName);
    }


    public Runnable runnableTask(final String taskName) {
        Preconditions.checkNotNull(taskName, "Task name cannot be null");
        return runnableTask(getByName(taskName));
    }

    public Runnable runnableTask(final Task task) {
        return new Runnable() {
            @Override
            public void run() {
                boolean isSucceed = false;
                switch (task.getTaskEnum()) {
                    case THEATER_UPDATE:
                        isSucceed = integrationService.updateTheaters();
                        break;
                    case SHOWTIME_UPDATE:
                        isSucceed = integrationService.updateMovieShowtime(true);
                        break;
                    case MOVIE_COMING_SOON:
                        isSucceed = integrationService.updateComingSoonMovie();
                        break;
                    case MOVIE_RATING_UPDATE:
                        break;
                    case MOVIE_INFORMATION_UPDATE:
                        break;
                    default:
                        return;
                }
                saveTaskExecutionHistory(task, isSucceed, "");
            }
        };
    }

    private void saveTaskExecutionHistory(final Task task, boolean isSucceed, String message) {
        task.addExecutionHistory(isSucceed, message);
        task.setLastExecutionDate(DateUtils.now());
        task.setNextExecutionDate(DateUtils.nextCronStartDate(task.getCron()));
        save(task);
        log.info("Task was successfully executed. task={} at={} next={}", task.getTaskEnum(),
                task.getLastExecutionDate(),
                task.getNextExecutionDate());
    }
}

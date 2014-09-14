package com.ninjas.movietime.service.manage;

import com.codahale.metrics.Timer;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.ninjas.movietime.core.domain.api.Task;
import com.ninjas.movietime.core.util.DateUtils;
import com.ninjas.movietime.core.util.MetricManager;
import com.ninjas.movietime.repository.manage.TaskRepository;
import com.ninjas.movietime.service.IntegrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ayassinov on 07/09/14.
 */
@Slf4j
@Service
public class TaskService {

    private final String className = this.getClass().getCanonicalName();

    private final TaskRepository taskRepository;
    private final IntegrationService integrationService;

    @Autowired
    public TaskService(TaskRepository taskRepository, IntegrationService integrationService) {
        this.taskRepository = taskRepository;
        this.integrationService = integrationService;
    }

    public Task save(Task task) {
        final Optional<Timer.Context> timer = MetricManager.startTimer(className, "save");
        try {
            return taskRepository.save(task);
        } finally {
            MetricManager.stopTimer(timer);
        }
    }

    public List<Task> all() {
        final Optional<Timer.Context> timer = MetricManager.startTimer(className, "all");
        try {
            return taskRepository.findAll();
        } finally {
            MetricManager.stopTimer(timer);
        }
    }

    public Optional<Task> getByName(String taskName) {
        final Optional<Timer.Context> timer = MetricManager.startTimer(className, "getByName");
        try {
            return Optional.fromNullable(taskRepository.findById(taskName));
        } finally {
            MetricManager.stopTimer(timer);
        }
    }

    @Async
    public void run(final String taskName) {
        Runnable runnable = runnableTask(taskName);
        runnable.run();
    }

    public Runnable runnableTask(final Task task) {
        Preconditions.checkNotNull(task, "Task cannot be null. A task name is not valid");
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
                saveTaskExecutionHistory(task, isSucceed);
            }
        };
    }

    private Runnable runnableTask(final String taskName) {
        Preconditions.checkNotNull(taskName, "Task name cannot be null");
        return runnableTask(getByName(taskName).orNull());
    }

    private void saveTaskExecutionHistory(final Task task, boolean isSucceed) {
        task.addExecutionHistory(isSucceed);
        task.setLastExecutionDate(DateUtils.nowServerDate());
        task.setNextExecutionDate(DateUtils.nextCronStartDate(task.getCron()));
        save(task);
        log.info("Task was successfully executed. task={} at={} next={}", task.getTaskEnum(),
                task.getLastExecutionDate(),
                task.getNextExecutionDate());
    }
}

/*
 * Copyright 2014 Parisian Ninjas
 *
 * Licensed under the MIT License;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://opensource.org/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ninjas.movietime;

import com.ninjas.movietime.core.domain.api.Task;
import com.ninjas.movietime.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ayassinov on 02/09/2014.
 */
@Slf4j
@Component
public class ScheduledTasks {


    private final TaskService taskService;
    private final TaskScheduler taskScheduler;

    @Autowired
    public ScheduledTasks(TaskService taskService, TaskScheduler taskScheduler) {
        this.taskService = taskService;
        this.taskScheduler = taskScheduler;
    }

    public void scheduleTasks(List<MovieTimeConfig.CronTask> tasks) {
        Task task;
        for (MovieTimeConfig.CronTask cronTask : tasks) {
            task = setupTask(cronTask);
            planTask(task);
        }
    }

    public void executeTask(final String task) {
        //taskExecutor.execute(taskService.runnableTask(task));
    }

    private Task setupTask(final MovieTimeConfig.CronTask cronTask) {
        final Task task = new Task(cronTask.getName(), cronTask.getCron());
        return taskService.save(task);
    }

    private void planTask(final Task task) {
        taskScheduler.schedule(taskService.runnableTask(task), new CronTrigger(task.getCron()));
        log.info("Task was successfully planned. task={} at={}", task.getTaskEnum(), task.getNextExecutionDate());
    }


}

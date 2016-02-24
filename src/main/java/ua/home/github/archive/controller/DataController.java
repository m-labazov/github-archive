package ua.home.github.archive.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ua.home.github.archive.api.Metrics;
import ua.home.github.archive.api.Task;
import ua.home.github.archive.api.TaskService;

import java.util.Collections;
import java.util.List;

@RestController
public class DataController {

    @Autowired
    private TaskService taskService;

    @RequestMapping(value = "/collect/{from}/{to}", method = RequestMethod.POST)
    public String collectEvents(@PathVariable("from") String from, @PathVariable("to") String to,
                                @CookieValue(value = "userId") String userId) {
        // TODO add parameters validation (format, range)
        return taskService.createTask(from, to, userId);
    }

    @RequestMapping(value = "/check/{taskId}", method = RequestMethod.GET)
    public String checkTaskStatus(@PathVariable("taskId") String id) {
        return taskService.getStatus(id);
    }

    @RequestMapping(value = "/tasks", method = RequestMethod.GET)
    public List<Task> getTasks(@CookieValue(value = "userId") String userId) {
        if (!StringUtils.hasLength(userId)) {
            return Collections.emptyList();
        }
        return taskService.getTasks(userId);
    }

    @RequestMapping(value = "/metrics/{taskId}", method = RequestMethod.GET)
    public Metrics getMetrics(@PathVariable("taskId") String taskId) {
        return taskService.getMetrics(taskId);
    }


}

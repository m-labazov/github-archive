package ua.home.github.archive.api;

import java.util.List;

public interface TaskService {

    String createTask(String from, String to, String userId);

    String getStatus(String id);

    List<Task> getTasks(String userId);

    Metrics getMetrics(String taskId);
}

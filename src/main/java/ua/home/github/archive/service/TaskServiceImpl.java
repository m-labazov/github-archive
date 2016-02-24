package ua.home.github.archive.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.home.github.archive.api.*;
import ua.home.github.archive.data.MetricsImpl;
import ua.home.github.archive.data.TaskImpl;
import ua.home.github.archive.repository.EventRepository;
import ua.home.github.archive.repository.TaskRepository;
import ua.home.github.archive.util.DateUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskWorker taskWorker;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private EventRepository eventRepository;

    @Override
    public String createTask(String from, String to, String userId) {
        String id = UUID.randomUUID().toString();
        TaskImpl task = new TaskImpl(id, from, to, userId, TaskStatus.IN_PROGRESS);
        taskRepository.create(task);
        taskWorker.processTask(task, success -> taskRepository.completeTask(id, TaskStatus.COMPLETE),
                                    failure -> taskRepository.completeTask(id, TaskStatus.FAILED));
        return id;
    }

    @Override
    public String getStatus(String id) {
        return taskRepository.getStatus(id).orElse("undefind");
    }

    @Override
    public List<Task> getTasks(String userId) {
        return taskRepository.getTasks(userId);
    }

    @Override
    public Metrics getMetrics(String taskId) {
        Task task = taskRepository.getTask(taskId);
        LocalDateTime from = DateUtil.parse(task.getFrom());
        LocalDateTime to = DateUtil.parse(task.getTo()).plusHours(1);
        List<String> top5Events = eventRepository.findTop5Events(from, to);
        List<String> top5Users = eventRepository.findTop5Users(from, to);
        List<String> top5Repos = eventRepository.findTop5Repos(from, to);
        long totalEventsNumber = eventRepository.findTotalEventNumber(from, to);
        return new MetricsImpl(top5Events, top5Users, top5Repos, totalEventsNumber);
    }
}

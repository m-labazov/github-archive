package ua.home.github.archive.data;

import ua.home.github.archive.api.Task;
import ua.home.github.archive.api.TaskStatus;

public class TaskImpl implements Task {
    private final String id;
    private final String from;
    private final String to;
    private final String userId;
    private final TaskStatus status;

    public TaskImpl(String id, String from, String to, String userId, TaskStatus status) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.userId = userId;
        this.status = status;
    }

    @Override
    public TaskStatus getStatus() {
        return status;
    }

    @Override
    public String getTo() {
        return to;
    }

    @Override
    public String getFrom() {
        return from;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getUserId() {
        return userId;
    }
}

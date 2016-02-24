package ua.home.github.archive.api;

public interface Task {
    TaskStatus getStatus();

    String getTo();

    String getFrom();

    String getId();

    String getUserId();
}

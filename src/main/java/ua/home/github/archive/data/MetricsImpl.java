package ua.home.github.archive.data;

import ua.home.github.archive.api.Metrics;

import java.util.List;

public class MetricsImpl implements Metrics {

    private final List<String> top5Events;
    private final List<String> top5Users;
    private final List<String> top5Repos;
    private final long totalEventsNumber;

    @Override
    public List<String> getTop5Events() {
        return top5Events;
    }

    @Override
    public List<String> getTop5Users() {
        return top5Users;
    }

    @Override
    public List<String> getTop5Repos() {
        return top5Repos;
    }

    @Override
    public long getTotalEventsNumber() {
        return totalEventsNumber;
    }

    public MetricsImpl(List<String> top5Events, List<String> top5Users, List<String> top5Repos, long totalEventsNumber) {
        this.top5Events = top5Events;
        this.top5Users = top5Users;
        this.top5Repos = top5Repos;
        this.totalEventsNumber = totalEventsNumber;
    }
}

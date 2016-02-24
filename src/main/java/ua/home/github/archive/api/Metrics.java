package ua.home.github.archive.api;

import java.util.List;

public interface Metrics {
    List<String> getTop5Events();

    List<String> getTop5Users();

    List<String> getTop5Repos();

    long getTotalEventsNumber();
}

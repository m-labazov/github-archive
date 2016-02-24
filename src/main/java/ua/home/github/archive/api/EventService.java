package ua.home.github.archive.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Future;

public interface EventService {
    Future<Boolean> collectEvents(String dateFrom);
}

package ua.home.github.archive.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.SuccessCallback;
import ua.home.github.archive.api.EventService;
import ua.home.github.archive.data.TaskImpl;
import ua.home.github.archive.util.DateUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class TaskWorker {

    @Autowired
    private EventService eventService;

    @Async
    public Future<Boolean> processTask(TaskImpl task, SuccessCallback<? super Boolean> success, FailureCallback failure) {
        String dateToText = task.getTo();
        String dateFromText = task.getFrom();
        if (StringUtils.isEmpty(dateToText)) {
            dateToText = dateFromText;
        }

        LocalDateTime fromDate = DateUtil.parse(dateFromText);
        LocalDateTime toDate = DateUtil.parse(dateToText).plusMinutes(1);

        List<Future<Boolean>> tasks = new ArrayList<>();
        for (LocalDateTime date = fromDate; date.isBefore(toDate); date = date.plusHours(1)) {
            String dateText = DateUtil.format(date);
            tasks.add(eventService.collectEvents(dateText));
        }
        tasks.forEach(future -> {
            try {
                future.get();
            } catch (Exception e) {
                // TODO process exception and log
                throw new RuntimeException(e);
            }
        });
        AsyncResult<Boolean> asyncResult = new AsyncResult<>(true);
        asyncResult.addCallback(success, failure);
        return asyncResult;
    }

}

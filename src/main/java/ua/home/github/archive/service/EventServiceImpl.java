package ua.home.github.archive.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import ua.home.github.archive.api.ActivityJournalService;
import ua.home.github.archive.api.EventService;
import ua.home.github.archive.repository.EventRepository;

import java.util.List;
import java.util.concurrent.Future;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private ActivityJournalService activityJournalService;
    @Autowired
    private GithubClient githubClient;

    @Override
    @Async("collectThreadPool")
    public Future<Boolean> collectEvents(String dateText) {
        if (activityJournalService.processActivity(dateText)) {
            List<String> records = githubClient.getRecords(dateText);
            eventRepository.save(records);
        }
        return new AsyncResult<>(true);
    }

}

package ua.home.github.archive.service;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.home.github.archive.api.ActivityJournalService;
import ua.home.github.archive.repository.ActivityJournalRepository;

import java.util.Set;

@Service
public class ActivityJournalServiceImpl implements ActivityJournalService, InitializingBean {
    @Autowired
    private ActivityJournalRepository activityJournalRepository;

    private Set<String> activities;

    @Override
    public boolean processActivity(String date) {
        synchronized (activities) {
            if (!activities.contains(date)) {
                activityJournalRepository.addActivity(date);
                activities.add(date);
                return true;
            }
        }
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        activities = activityJournalRepository.getActivities();
    }
}

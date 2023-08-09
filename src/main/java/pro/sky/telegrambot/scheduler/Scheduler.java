package pro.sky.telegrambot.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pro.sky.telegrambot.entity.NotificationTask;
import pro.sky.telegrambot.repository.TaskRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Scheduler {
    @Autowired
    TaskRepository repository;

    @Scheduled(cron = " 0 0/1 * * * *")
    public List<NotificationTask> runTask() {
        return repository.findAll().stream()
                .filter(t -> t.getTime().equals(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)))
                .collect(Collectors.toList());
    }
}

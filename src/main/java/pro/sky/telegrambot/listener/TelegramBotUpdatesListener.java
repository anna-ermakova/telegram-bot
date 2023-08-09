package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.entity.NotificationTask;
import pro.sky.telegrambot.repository.TaskRepository;
import pro.sky.telegrambot.scheduler.Scheduler;

import javax.annotation.PostConstruct;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;
    @Autowired
    private TaskRepository repository;
    @Autowired
    Scheduler scheduler;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            if (update.message().text().equals("/start")) {
                SendMessage message = new SendMessage(update.message().chat().id(),
                        "Hi, " + update.message().from().firstName() + ", nice to meat you!");
                SendResponse response = telegramBot.execute(message);
                logger.info("the message has been sent");
            } else {
                String task = update.message().text();
                logger.info(task);
                Pattern pattern = Pattern.compile("([0-9.:\\s]{16})(\\s)([\\W+]+)");
                Matcher matcher = pattern.matcher(task);
                if (matcher.matches()) {
                    System.out.println(matcher.group(1));
                    System.out.println(matcher.group(3));

                    LocalDateTime date = LocalDateTime.parse(matcher.group(1),
                            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
                    String item = matcher.group(3);
                    System.out.println(date);
                    System.out.println(item);

                    NotificationTask notificationTask = new NotificationTask();
                    notificationTask.setChatId(update.message().chat().id());
                    notificationTask.setTime(date);
                    notificationTask.setMessage(item);
                    repository.save(notificationTask);
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Scheduled(cron = " 0 0/1 * * * *")
    public void checkTask() {
        scheduler.runTask()
                .forEach((notificationTask) -> {
                    SendMessage message = new SendMessage(notificationTask.getChatId(), notificationTask.getMessage());
                    SendResponse response = telegramBot.execute(message);
                });
    }
}

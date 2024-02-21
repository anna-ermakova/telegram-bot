package pro.sky.telegrambot.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "notification_tasks")
public class NotificationTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 4096, nullable = false)
    private String text;

    @Column(name = "chat_id")
    private long chatId;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    public NotificationTask(String text, long chatId, LocalDateTime dateTime) {
        this.text = text;
        this.chatId = chatId;
        this.dateTime = dateTime;
    }

    public NotificationTask() {
    }
}
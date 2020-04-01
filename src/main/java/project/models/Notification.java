package project.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "notification")
public class Notification
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JoinColumn(nullable = false)
    @OneToOne
    private NotificationType notificationType;

    @Column(name = "sent_time", updatable = false, nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date sentTime;

    @JoinColumn(name = "person_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Person person;

    @JoinColumn(name = "entity_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private MainEntity mainEntity;

    private String contact;
}

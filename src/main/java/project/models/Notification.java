package project.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "notification")
@EqualsAndHashCode(exclude = "person")
public class Notification
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer typeId;

    @Column(name = "sent_time", updatable = false, nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date sentTime;

    @JoinColumn(name = "person_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Person person;

    @Column(name = "entity_id")
    private Integer entityId;

    private String contact;
}

package project.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import project.models.enums.ReadStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime time;

    @Column(name = "autor_id")
    private Integer authorId;

    @Column(name = "recipient_id")
    private Integer recipientId;

    @Column(name = "message_text")
    private String messageText;

    @Column(name = "read_status")
    @Enumerated(EnumType.STRING)
    private ReadStatus readStatus;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "dialog_id", nullable = false)
    private Dialog dialog;
}

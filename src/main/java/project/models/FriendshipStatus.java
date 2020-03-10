package project.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import project.models.enums.Code;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "friendship_status")
public class FriendshipStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime time;

    private String name;

    @Enumerated(EnumType.STRING)
    private Code code;

    @OneToOne(mappedBy = "statusId")
    private Friendship friendship;
}

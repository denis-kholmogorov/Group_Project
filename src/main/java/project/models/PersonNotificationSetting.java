package project.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Data
@Entity
@Table(name = "person_notification_settings")
@NoArgsConstructor
public class PersonNotificationSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @Column(name = "person_id")
    //@ManyToOne(fetch = FetchType.LAZY)
    private Integer personId;

    @OneToOne
    @JoinColumn(name = "notification_type_id")
    private NotificationType notificationType;

    @Column(name = "enable")
    @Type(type = "yes_no")
    private Boolean enable;

    public PersonNotificationSetting(Integer personId, NotificationType notificationType, Boolean enable) {
        this.personId = personId;
        this.notificationType = notificationType;
        this.enable = enable;
    }
}

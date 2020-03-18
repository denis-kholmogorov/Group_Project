package project.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.Type;
import project.models.enums.MessagesPermission;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "first_name")
    @JsonProperty("first_name")
    private String firstName;

    @Column(name = "last_name")
    @JsonProperty("last_name")
    private String lastName;

    @Column(updatable = false, name = "reg_date")
    @JsonProperty("reg_date")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date regDate;

    @Column(name = "birth_date")
    @JsonProperty("birth_date")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date birthDate;

    private String email;

    private String phone;

    @JsonIgnore
    private String password;

    private String photo;

    private String about;

    private String city;

    private String country;

    @Column(name = "confirmation_code")
    @JsonIgnore
    private String confirmationCode;

    @Column(name = "is_approved")
    @Type(type = "yes_no")
    @JsonIgnore
    private Boolean isApproved;

    @Column(name = "messages_permission")
    @Enumerated(EnumType.STRING)
    @JsonProperty("messages_permission")
    private MessagesPermission messagesPermission;

    @Column(name = "last_online_time")
    @JsonProperty("last_online_time")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date lastOnlineTime;

    @Column(name = "is_blocked")
    @Type(type = "yes_no")
    @JsonProperty("is_blocked")
    private boolean isBlocked;
}

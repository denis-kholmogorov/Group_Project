package project.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.Type;
import project.models.enums.MessagesPermission;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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

    @Column(name = "e_mail")
    private String email;

    private String phone;

    @JsonIgnore
    private String password;

    private String photo;

    private String about;

    private String city;

    private String country;

    @JsonIgnore
    @Column(name = "confirmation_code")
    private String confirmationCode;

    @JsonIgnore
    @Column(name = "is_approved")
    @Type(type = "yes_no")
    private Boolean isApproved;

    @JsonProperty("messages_permission")
    @Column(name = "messages_permission")
    @Enumerated(EnumType.STRING)
    private MessagesPermission messagesPermission;

    @JsonProperty("last_online_time")
    @Column(name = "last_online_time")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date lastOnlineTime;

    @JsonProperty("is_blocked")
    @Column(name = "is_blocked")
    @Type(type = "yes_no")
    private boolean isBlocked;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns =@JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles = new HashSet<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "persons", fetch = FetchType.LAZY)
    private Set<Dialog> dialogs;

    @PreRemove
    public void removeUser() {
        roles.forEach(role -> role.getUsers().remove(this));
    }
}

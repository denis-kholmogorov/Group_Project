package project.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.Type;
import project.models.enums.MessagesPermission;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(updatable = false, name = "reg_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime regDate;

    @Column(name = "birth_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate birthDate;

    @Column(name = "e_mail")
    private String email;

    private String phone;

    private String password;

    private String photo;

    private String about;

    private String town;

    @Column(name = "confirmation_code")
    private String confirmationCode;

    @Column(name = "is_approved")
    @Type(type = "yes_no")
    private Boolean isApproved;

    @Column(name = "messages_permission")
    @Enumerated(EnumType.STRING)
    private MessagesPermission messagePermission;

    @Column(name = "last_online_time")
    private LocalDateTime lastOnlineTime;

    @Column(name = "is_blocked")
    @Type(type = "yes_no")
    private boolean isBlocked;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "token_id", referencedColumnName = "id")
    private Token token;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles",
    joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
    inverseJoinColumns =@JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles = new HashSet<>();
}

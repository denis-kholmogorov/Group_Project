package project.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.ManyToAny;
import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import project.models.enums.MessagesPermission;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;


@Data
@Entity
@Table(name = "person")
public class Person implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(updatable = false, name = "reg_date")
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss")
    private long regDate;

    @Column(name = "birth_date")
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private long birthDate;

    @Column(name = "e_mail")
    private String email;

    private String phone;

    private String password;

    private String photo;

    private String about;

    @OneToOne
    private City city;

    @OneToOne
    private Country country;

    @Column(name = "confirmation_code")
    private String confirmationCode;

    @Column(name = "is_approved")
    @Type(type = "yes_no")
    private Boolean isApproved = true;

    @Column(name = "messages_permission")
    @Enumerated(EnumType.STRING)
    private MessagesPermission messagePermission;

    @Column(name = "last_online_time")
    private LocalDateTime lastOnlineTime = LocalDateTime.now();

    @Column(name = "is_blocked")
    @Type(type = "yes_no")
    private boolean isBlocked = false;

    private String token;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

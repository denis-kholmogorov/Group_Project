package project.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;


@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {
    @Setter
    @Getter
    @Id
    private Integer id;

    @Setter
    @Getter
    private String name;

    @Setter
    @Getter
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<Person> users;

    @Override
    public String getAuthority() {
        return getName();
    }
}

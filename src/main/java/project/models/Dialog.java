package project.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
@ToString(exclude = "listMessage")
@Table(name = "dialog")
public class Dialog
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "dialog_user",
            joinColumns = {@JoinColumn(name = "dialog_id", referencedColumnName = "id")},
            inverseJoinColumns =@JoinColumn(name = "person_id", referencedColumnName = "id"))
    private Set<Person> persons = new HashSet<>();

    @PreRemove
    public void removeUser() {
        persons.forEach(person -> person.getDialogs().remove(this));
    }

    //@JsonIgnore
    //Comparator<Message> comparator = Comparator.comparing(m -> m.getTime().getTime());

    @JsonIgnore
    @OneToMany(mappedBy = "dialog", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Message> listMessage = new ArrayList<>();

}

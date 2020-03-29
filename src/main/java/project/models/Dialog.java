package project.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
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

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)   //example
    @JoinColumn(name = "dialog_id")
    @Where(clause = "read_status = 'SENT'")
    private List<Message> unread = new ArrayList<>();

    //@JsonIgnore
    //Comparator<Message> comparator = Comparator.comparing(m -> m.getTime().getTime());

    @JsonIgnore
    @OneToMany(mappedBy = "dialog", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Message> listMessage = new ArrayList<>();

}

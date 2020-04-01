package project.models;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class MainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Getter
    private Integer id;

    @Getter
    @OneToMany(mappedBy = "mainEntity")
    private List<Notification> notifications = new ArrayList<>();
}

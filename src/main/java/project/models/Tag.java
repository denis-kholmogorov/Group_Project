package project.models;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "tag")
public class Tag
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String tag;

    @ManyToMany(mappedBy = "tagList", fetch = FetchType.LAZY)
    private List<Post> postList = new ArrayList<>();
}

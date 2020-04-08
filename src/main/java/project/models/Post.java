package project.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@ToString(exclude = "tagList")
@Table(name = "post")
public class Post extends MainEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Integer id;

    @Column(updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date time;

    @ManyToOne
    @JoinColumn(name = "author_Id", nullable = false)
    private Person author;

    private String title;

    @Column(name = "post_text")
    private String postText;

    @Column(name = "id_blocked")
    @Type(type = "yes_no")
    private Boolean isBlocked;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "post2tag",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    List<Tag> tagList = new ArrayList<>();

    @PreRemove
    public void removeTags() {
        tagList.forEach(tag -> tag.getPostList().remove(this));
    }
}

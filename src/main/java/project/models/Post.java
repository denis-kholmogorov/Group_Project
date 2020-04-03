package project.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
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
}

package project.models;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "post_comments")
public class PostComments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime time;

    @Column(name = "post_id")
    private Integer postId;

    @Column(name = "parent_id")
    private Integer parentId;

    @Column(name = "author_id")
    private Integer authorId;

    @Column(name = "comment", nullable = false, columnDefinition = "text")
    private String comment;

    @Column(name = "is_blocked")
    @Type(type = "yes_no")
    private Boolean isBlocked;
}


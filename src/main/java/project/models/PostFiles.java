package project.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "post_file")
public class PostFiles
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(updatable = false, nullable = false)
    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime time;

    @Column(name = "post_id")
    private Integer post;

    @Column(name = "parent_id")
    private Integer parentId;

    @Column(name = "author_id")
    private Integer authorId;

    @Column(name = "comment_text", nullable = false)
    private String text;

    @Column(name = "is_blocked")
    @Type(type = "yes_no")
    private Boolean isBlocked;
}

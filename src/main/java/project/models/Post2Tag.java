package project.models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "post2tag")
public class Post2Tag
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "post_id", nullable = false)
    private Integer postId;

    @Column(name = "tag_id", nullable = false)
    private Integer tag;
}

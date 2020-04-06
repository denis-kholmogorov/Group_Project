package project.models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "friendship")
public class Friendship extends MainEntity {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "status_id")
    private FriendshipStatus status;

    @JoinColumn(name = "src_person_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Person srcPerson;

    @JoinColumn(name = "dst_person_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Person dstPerson;
}

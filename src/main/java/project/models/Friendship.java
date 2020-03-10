package project.models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "friendship")
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "status_id")
    private FriendshipStatus statusId;

    @Column(name = "src_person_id")
    private Integer PersonIdWhoTakeFriendship;

    @Column(name = "dst_person_id")
    private Integer PersonIdWhoSendFriendship;
}

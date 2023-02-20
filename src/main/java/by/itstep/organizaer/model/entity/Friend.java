package by.itstep.organizaer.model.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Friend {

    private static final String SEQ_NAME = "friend_id_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_NAME)
    @SequenceGenerator(name = SEQ_NAME, sequenceName = SEQ_NAME, allocationSize = 1)
    Long id;

    UUID uuid;

    String name;

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    Contacts contacts;

    LocalDate birthday;

    @ManyToOne
    User user;

    @ManyToOne
    @JoinColumn(name = "friend_group")
    FriendGroup friendGroup;
}

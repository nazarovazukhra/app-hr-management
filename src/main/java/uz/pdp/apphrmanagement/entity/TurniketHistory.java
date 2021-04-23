package uz.pdp.apphrmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.apphrmanagement.entity.enums.TurniketType;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TurniketHistory {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private Turniket turniket;

    @ManyToOne
    private User user;

    private Timestamp turniketTime;

    @Enumerated
    private TurniketType turkinetType;
}

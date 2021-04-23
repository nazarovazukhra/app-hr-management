package uz.pdp.apphrmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Salary {

    @Id
    @GeneratedValue()
    private UUID id;

    @ManyToOne
    private User userId;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private Date date;

    private Date workStartDate;

    private Date workEndDate;

}

package uz.pdp.apphrmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Task {


    @Id
    @GeneratedValue()
    private UUID id; // task ning takrorlanmas qismi


    @Column(nullable = false)
    private String taskName; // topshiriq nomi


    private String description; // topshiriq tavsiloti

    @Column(nullable = false)
    private Timestamp deadline;

    @Column(nullable = false)
    private Timestamp createdAt;

    private String status;

    @ManyToOne
    private User createdBy;

    @ManyToOne
    private User resId; // it is ID of responsible person for task

}

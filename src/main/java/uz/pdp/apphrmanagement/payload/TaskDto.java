package uz.pdp.apphrmanagement.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {

    private String taskName;
    private String description;
    private Timestamp deadline;
    private Timestamp createdAt;
    private Boolean status;
    private UUID createdBy;
    private UUID resId;
}

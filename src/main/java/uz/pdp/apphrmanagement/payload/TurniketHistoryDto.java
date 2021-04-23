package uz.pdp.apphrmanagement.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TurniketHistoryDto {

    private UUID turniketId;
    private UUID userId;
    private Timestamp turniketTime;
}

package uz.pdp.apphrmanagement.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalaryDto {

    private UUID userId;
    private Double amount;
    private Date date;
    private Date workStartDate;
    private Date workEndDate;
}

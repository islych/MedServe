package com.medical.appointment.appointment.DTO;


import com.medical.appointment.appointment.Model.enums.Statut;
import com.medical.appointment.appointment.Model.enums.Urgency;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RendezvousUpdateDto {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
    private String object;
    private String description;
    private Urgency urgency;
    private Statut statut;

}

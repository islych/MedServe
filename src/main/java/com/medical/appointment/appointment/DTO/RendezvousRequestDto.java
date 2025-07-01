package com.medical.appointment.appointment.DTO;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.medical.appointment.appointment.Model.enums.Urgency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;



@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RendezvousRequestDto{
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;
    private String object;
    private String description;
    private Urgency urgency;
    private Long doctorId;
}
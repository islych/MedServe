// Dans le fichier CalendarEventDTO.java
package com.medical.appointment.appointment.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class CalendarEventDTO {
    private Long id;
    private String title;
    private Date start;
    private String status;
 
}
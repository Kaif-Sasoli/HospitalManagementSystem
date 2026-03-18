package com.example.hospitalmanagementsystem.DTO;

import com.example.hospitalmanagementsystem.entity.type.AppointmentTiming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentRequestDto {
    private Long doctorId;
    private AppointmentTiming scheduled;
}

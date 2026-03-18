package com.example.hospitalmanagementsystem.entity;

import com.example.hospitalmanagementsystem.entity.type.AppointmentStatus;

import com.example.hospitalmanagementsystem.entity.type.AppointmentTiming;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "appointments")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Enumerated(EnumType.STRING)
    private AppointmentTiming schecduled;

    private Double consultationFee;

    private boolean paid;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus appointmentStatus;

    private LocalDate createdAt;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @OneToOne
    @JoinColumn(name = "report_id")
    private Report report;

    @ManyToOne
    @JoinColumn(name = "history_id")
    private History history;

}

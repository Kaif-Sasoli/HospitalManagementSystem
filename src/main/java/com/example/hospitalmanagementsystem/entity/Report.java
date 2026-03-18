package com.example.hospitalmanagementsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "reports")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Report {

    @Id
    private Long id;

    private LocalDate generationTime;

    @OneToOne
    @JoinColumn(name = "prescription_id")
    private Prescription prescription;

    private String recommendation;

    private String disease;

    @OneToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @ManyToOne
    @JoinColumn(name = "history_id")
    private History history;


}


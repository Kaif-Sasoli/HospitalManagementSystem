package com.example.hospitalmanagementsystem.entity;

import com.example.hospitalmanagementsystem.entity.type.GenderType;
import com.example.hospitalmanagementsystem.entity.type.BloodGroupType;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.LocalDate;
import java.util.List;

@Entity
@Builder
@Data
@Table(name = "patients")
@AllArgsConstructor
@NoArgsConstructor
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Patient name cannot be blank")
    private String name;

    @Email(message = "Provide a valid email")
    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private GenderType genderType;

    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private BloodGroupType bloodGroupType;

    @CurrentTimestamp
    @Column(updatable = false)
    private LocalDate createdAt;

    @OneToOne
    @MapsId
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    private History history;

    @OneToMany(
            mappedBy = "patient",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Appointment> appointments;



}
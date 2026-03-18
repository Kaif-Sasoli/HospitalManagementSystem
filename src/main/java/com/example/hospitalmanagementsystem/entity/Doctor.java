package com.example.hospitalmanagementsystem.entity;

import com.example.hospitalmanagementsystem.entity.type.RoleType;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "doctors")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Email(message = "Provide a valid email")
    @Column(unique = true, nullable = false)
    private String email;

    @CurrentTimestamp
    private LocalDate createdAt;

    @OneToOne
    @MapsId
    private User user;

    @ManyToOne
    @JoinColumn(name = "dept_id")
    private Department department;

    @OneToMany(mappedBy = "doctor")
    private List<Appointment> appointments;

}


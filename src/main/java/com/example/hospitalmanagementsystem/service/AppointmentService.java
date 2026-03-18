package com.example.hospitalmanagementsystem.service;

import com.example.hospitalmanagementsystem.DTO.ApiResponse;
import com.example.hospitalmanagementsystem.DTO.AppointmentRequestDto;
import com.example.hospitalmanagementsystem.entity.*;
import com.example.hospitalmanagementsystem.entity.type.AppointmentStatus;
import com.example.hospitalmanagementsystem.exception.UserNotFoundException;
import com.example.hospitalmanagementsystem.repository.AppointmentRepository;
import com.example.hospitalmanagementsystem.repository.DoctorRepository;
import com.example.hospitalmanagementsystem.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final PatientRepository patientRepository;

    private final DoctorRepository doctorRepository;

    private final AppointmentRepository appointmentRepository;

    public ApiResponse makeAppointment(User user, AppointmentRequestDto appointmentRequestDto) {

        Patient patient = patientRepository.findByUser(user)
                .orElseThrow(() -> new UserNotFoundException("Patient not found"));
        Doctor doctor = doctorRepository.findById(appointmentRequestDto.getDoctorId())
                .orElseThrow(() -> new UserNotFoundException("Doctor not found"));

        History history = patient.getHistory();

        if(history == null) {
            history = History.builder()
                    .appointments(new ArrayList<>())
                    .reports(new ArrayList<>())
                    .build();

            patient.setHistory(history);
        }

        Appointment appointment = Appointment.builder()
                        .schecduled(appointmentRequestDto.getScheduled())
                        .paid(false)
                        .appointmentStatus(AppointmentStatus.IN_PROGRESS)
                        .patient(patient)
                        .doctor(doctor)
                        .createdAt(LocalDate.now())
                        .build();

        history.addAppointment(appointment);

        appointmentRepository.save(appointment);
        return new ApiResponse(true, "Success", "Appointment created successfully");
    }
}

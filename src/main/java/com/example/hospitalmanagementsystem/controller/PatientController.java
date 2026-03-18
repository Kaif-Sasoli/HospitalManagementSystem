package com.example.hospitalmanagementsystem.controller;

import com.example.hospitalmanagementsystem.DTO.ApiResponse;
import com.example.hospitalmanagementsystem.DTO.AppointmentRequestDto;
import com.example.hospitalmanagementsystem.entity.User;
import com.example.hospitalmanagementsystem.service.AppointmentService;
import com.example.hospitalmanagementsystem.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patient")
public class PatientController {

    private final AppointmentService appointmentService;

    @PostMapping("/makeappointment")
    public ResponseEntity<ApiResponse> makeAppointment(
            @AuthenticationPrincipal User user,
            @RequestBody AppointmentRequestDto appointmentRequestDto){
         return new ResponseEntity<>(
                 appointmentService.makeAppointment(user, appointmentRequestDto),
                 HttpStatus.CREATED
         );
    }




}

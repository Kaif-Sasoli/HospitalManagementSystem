package com.example.hospitalmanagementsystem.service;

import com.example.hospitalmanagementsystem.DTO.ApiResponse;
import com.example.hospitalmanagementsystem.DTO.DoctorResponseDto;
import com.example.hospitalmanagementsystem.DTO.InsertDoctorDto;
import com.example.hospitalmanagementsystem.entity.Appointment;
import com.example.hospitalmanagementsystem.entity.Department;
import com.example.hospitalmanagementsystem.entity.Doctor;
import com.example.hospitalmanagementsystem.entity.User;
import com.example.hospitalmanagementsystem.entity.type.AuthProviderType;
import com.example.hospitalmanagementsystem.entity.type.DepartmentType;
import com.example.hospitalmanagementsystem.entity.type.RoleType;
import com.example.hospitalmanagementsystem.exception.DepartmentNotFound;
import com.example.hospitalmanagementsystem.exception.UserNotFoundException;
import com.example.hospitalmanagementsystem.repository.AppointmentRepository;
import com.example.hospitalmanagementsystem.repository.DepartmentRepository;
import com.example.hospitalmanagementsystem.repository.DoctorRepository;
import com.example.hospitalmanagementsystem.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final UserRepository userRepository;

    private final DoctorRepository doctorRepository;

    private final AppointmentRepository appointmentRepository;

    private final DepartmentRepository departmentRepository;

    @Transactional
    public String addDoctor(InsertDoctorDto insertDoctorDto) {

        Department dept = departmentRepository.findByDepartmentName(
                        DepartmentType.valueOf(insertDoctorDto.getDepartment().toString())
                )
                .orElseThrow(() ->  new DepartmentNotFound("Department doesn't exists!"));

        User user = User.builder()
                .username(insertDoctorDto.getUsername())
                .authProviderType(AuthProviderType.EMAIL)
                .roles(Set.of(RoleType.DOCTOR))
                .build();

        userRepository.save(user);

        Doctor doctor = Doctor.builder()
                .name(insertDoctorDto.getName())
                .email(insertDoctorDto.getEmail())
                .user(user)
                .department(dept)
                .build();

        dept.addDoctor(doctor);
        doctorRepository.save(doctor);

        return "Done";
    }

    List<Doctor> doctors;
    public ApiResponse<List<DoctorResponseDto>> getAllDoctors(DepartmentType departmentType) {

          if(departmentType != null) {
               doctors = doctorRepository.findByDepartmentDepartmentName(departmentType);
          } else {
              doctors = doctorRepository.findAll();
          }

          List<DoctorResponseDto> response = doctors.stream()
                  .map(this::toDto)
                  .toList();

          return new ApiResponse<>(true, "Doctors Fetched", response);

    }

    public ApiResponse<List<Appointment>> getAppointments(User user) {

        Doctor doctor = doctorRepository.findByUser(user)
                .orElseThrow(() -> new UserNotFoundException("Doctor not found!"));
         List<Appointment> appointments = appointmentRepository
                 .findAllByDoctor(doctor);

         return  new ApiResponse<>(true, "Appointments Fetched", appointments);

    }


    private DoctorResponseDto toDto(Doctor doctor) {
        return DoctorResponseDto.builder()
                .id(doctor.getId())
                .name(doctor.getName())
                .departmentType(doctor.getDepartment().getDepartmentName())
                .build();
    }
}

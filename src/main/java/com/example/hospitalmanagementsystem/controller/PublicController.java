package com.example.hospitalmanagementsystem.controller;

import com.example.hospitalmanagementsystem.DTO.ApiResponse;
import com.example.hospitalmanagementsystem.DTO.DoctorResponseDto;
import com.example.hospitalmanagementsystem.entity.type.DepartmentType;
import com.example.hospitalmanagementsystem.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public")
public class PublicController {

    private final DoctorService doctorService;


    @GetMapping("/getdoctors")
    public ApiResponse<List<DoctorResponseDto>> getAllDoctors(
            @RequestParam(required = false) DepartmentType departmentType
            ) {
        return doctorService.getAllDoctors(departmentType);
    }

}

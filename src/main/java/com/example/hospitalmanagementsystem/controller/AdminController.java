package com.example.hospitalmanagementsystem.controller;

import com.example.hospitalmanagementsystem.DTO.ApiResponse;
import com.example.hospitalmanagementsystem.DTO.InsertDeptDto;
import com.example.hospitalmanagementsystem.DTO.InsertDoctorDto;
import com.example.hospitalmanagementsystem.service.AdminService;
import com.example.hospitalmanagementsystem.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private DoctorService doctorService;

    @PostMapping("/adddept")
    public ResponseEntity<ApiResponse> addDepartment(@RequestBody InsertDeptDto department) {
         return new ResponseEntity<>(adminService.addDepartment(department), HttpStatus.CREATED);
    }

    @DeleteMapping("/deletedept/{id}")
    public ResponseEntity<ApiResponse> deleteDepartment(@PathVariable Long id){
          return new ResponseEntity<>(adminService.deleteDepartment(id), HttpStatus.OK);
    }

    @PostMapping("/adddoctor")
    public ResponseEntity<String> addDoctor(@RequestBody InsertDoctorDto insertDoctorDto) {
            return new ResponseEntity<>(doctorService.addDoctor(insertDoctorDto), HttpStatus.CREATED);
    }

}

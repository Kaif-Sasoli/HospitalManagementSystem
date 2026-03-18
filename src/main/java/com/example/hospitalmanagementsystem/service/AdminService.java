package com.example.hospitalmanagementsystem.service;

import com.example.hospitalmanagementsystem.DTO.ApiResponse;
import com.example.hospitalmanagementsystem.DTO.InsertDeptDto;
import com.example.hospitalmanagementsystem.entity.Department;
import com.example.hospitalmanagementsystem.entity.type.DepartmentType;
import com.example.hospitalmanagementsystem.exception.DepartmentNotFound;
import com.example.hospitalmanagementsystem.repository.DepartmentRepository;
import com.example.hospitalmanagementsystem.repository.DoctorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AdminService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DepartmentRepository departmentRepository;


        public ApiResponse<Void> addDepartment(InsertDeptDto department) {
//            departmentRepository.findByDepartmentName(
//                     DepartmentType.valueOf(department.getDepartmentName().toString())
//            ).orElseThrow(() -> new RuntimeException("Department already exists"));
            Department dept = Department.builder()
                    .departmentName(department.getDepartmentName())
                    .build();

             departmentRepository.save(dept);
             return new ApiResponse<>(
                     true, "Department created successfully", null);
        }

    public ApiResponse<Void> deleteDepartment(Long id) {

           departmentRepository.findById(id)
                   .orElseThrow(()-> new DepartmentNotFound("Department not found!"));

           departmentRepository.deleteById(id);

           return new ApiResponse<>(true, "Department deleted successfully", null);

    }
}

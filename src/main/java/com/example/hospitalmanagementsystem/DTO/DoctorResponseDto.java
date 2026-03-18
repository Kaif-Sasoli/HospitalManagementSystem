package com.example.hospitalmanagementsystem.DTO;

import com.example.hospitalmanagementsystem.entity.type.DepartmentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorResponseDto {
    private Long id;
    private String name;
    private String email;
    private DepartmentType departmentType;
}

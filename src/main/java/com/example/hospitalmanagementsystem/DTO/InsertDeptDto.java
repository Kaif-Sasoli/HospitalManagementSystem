package com.example.hospitalmanagementsystem.DTO;

import com.example.hospitalmanagementsystem.entity.type.DepartmentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InsertDeptDto {
    private DepartmentType departmentName;
}

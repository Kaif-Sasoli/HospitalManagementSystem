package com.example.hospitalmanagementsystem.repository;

import com.example.hospitalmanagementsystem.entity.Department;
import com.example.hospitalmanagementsystem.entity.type.DepartmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    
    Optional<Department> findByDepartmentName(DepartmentType name);

    boolean existsByDepartmentName(DepartmentType name);

    Optional<Department> findByDeptId(Long id);
}

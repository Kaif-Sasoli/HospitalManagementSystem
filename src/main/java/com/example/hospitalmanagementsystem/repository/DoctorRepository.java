package com.example.hospitalmanagementsystem.repository;

import com.example.hospitalmanagementsystem.entity.Doctor;
import com.example.hospitalmanagementsystem.entity.User;
import com.example.hospitalmanagementsystem.entity.type.DepartmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findByDepartmentDepartmentName(DepartmentType departmentType);

    Optional<Doctor> findByUser(User user);
}

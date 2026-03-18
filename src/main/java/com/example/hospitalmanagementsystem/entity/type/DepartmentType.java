package com.example.hospitalmanagementsystem.entity.type;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum DepartmentType {
    CARDIOLOGY,
    NEUROLOGY,
    ORTHOPEDICS,
    PEDIATRICS,
    GENERAL_MEDICINE;

    @JsonCreator
    public static DepartmentType from(String value) {
        return DepartmentType.valueOf(value.toUpperCase());
    }
}

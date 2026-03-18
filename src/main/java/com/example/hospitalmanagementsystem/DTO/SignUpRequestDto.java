package com.example.hospitalmanagementsystem.DTO;

import com.example.hospitalmanagementsystem.entity.type.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequestDto {
    private String username;
    private String password;
    private String name;
    private String email;

    private Set<RoleType> roles = new HashSet<>();


}

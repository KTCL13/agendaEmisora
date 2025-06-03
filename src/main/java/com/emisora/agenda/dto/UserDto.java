package com.emisora.agenda.dto;

import java.util.List;

import com.emisora.agenda.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserDto {

    private Long id;
    private String username;
    private String email;
    private List<Role> roles;

    private boolean active;
}

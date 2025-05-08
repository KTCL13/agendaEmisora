package com.emisora.agenda.dto;

import com.emisora.agenda.model.Role;

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
    private String login;
    private String token;
    private String name;
    private String email;
    private Role role;

}

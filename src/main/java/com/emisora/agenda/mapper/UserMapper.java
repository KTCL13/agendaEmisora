package com.emisora.agenda.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.emisora.agenda.dto.SignUpDto;
import com.emisora.agenda.dto.UserDto;
import com.emisora.agenda.enums.Role;
import com.emisora.agenda.model.User;

@Mapper(componentModel = "spring")
public interface  UserMapper {


    UserDto toUserDto(User user);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "active", constant = "true")
    User signUpDtoToUser(SignUpDto signUpDto);

    default Role map(List<Role> roles) {
        return roles != null && !roles.isEmpty() ? roles.get(0) : null;
    }
}

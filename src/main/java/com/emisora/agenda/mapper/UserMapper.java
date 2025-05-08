package com.emisora.agenda.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.emisora.agenda.dto.SignUpDto;
import com.emisora.agenda.dto.UserDto;
import com.emisora.agenda.model.User;

@Mapper(componentModel = "spring")
public interface  UserMapper {

    UserDto toUserDto(User user);

    @Mapping(target = "password", ignore = true)
    User signUpDtoToUser(SignUpDto signUpDto);


}

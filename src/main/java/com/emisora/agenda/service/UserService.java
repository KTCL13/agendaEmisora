package com.emisora.agenda.service;

import java.nio.CharBuffer;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.emisora.agenda.dto.CredentialsDto;
import com.emisora.agenda.dto.SignUpDto;
import com.emisora.agenda.dto.UserDto;
import com.emisora.agenda.exceptions.AppException;
import com.emisora.agenda.mapper.UserMapper;
import com.emisora.agenda.model.User;
import com.emisora.agenda.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserDto findByLogin(String login) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
        return userMapper.toUserDto(user);
    }

    public UserDto login(CredentialsDto credentialsDto){
        User user = userRepository.findByLogin(credentialsDto.getLogin())
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        if(passwordEncoder.matches(CharBuffer.wrap(credentialsDto.getPassword()), user.getPassword())){
            return userMapper.toUserDto(user);
        }

        throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
    }

    
    public UserDto register(SignUpDto userDto) {

        Optional<User> userOptional = userRepository.findByLogin(userDto.getLogin());

        if (userOptional.isPresent()) {
            throw new AppException("User already exists", HttpStatus.BAD_REQUEST);
        }

        User user =  userMapper.signUpDtoToUser(userDto);

        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        User savedUser = userRepository.save(user);

        return userMapper.toUserDto(savedUser);
    }

}

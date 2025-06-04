package com.emisora.agenda.service;

import com.emisora.agenda.dto.CredentialsDto;
import com.emisora.agenda.dto.SignUpDto;
import com.emisora.agenda.dto.UserDto;
import com.emisora.agenda.exceptions.AppException;
import com.emisora.agenda.mapper.UserMapper;
import com.emisora.agenda.model.User;
import com.emisora.agenda.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock private UserRepository userRepository;
    @Mock private UserMapper userMapper;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDto userDto;
    private CredentialsDto credentialsDto;
    private SignUpDto signUpDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("usuario");
        user.setPassword("hashedPassword");

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("usuario");

        credentialsDto = new CredentialsDto();
        credentialsDto.setUsername("usuario");
        credentialsDto.setPassword("password123".toCharArray());

        signUpDto = new SignUpDto();
        signUpDto.setUsername("nuevoUsuario");
        signUpDto.setPassword("newPass123");
    }

    @Test
    void encuentraUsuarioPorLogin() {
        when(userRepository.findByUsername("usuario")).thenReturn(Optional.of(user));
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        UserDto result = userService.findUserByUsername("usuario");

        assertNotNull(result);
        assertEquals(userDto.getUsername(), result.getUsername());

        verify(userRepository).findByUsername("usuario");
        verify(userMapper).toUserDto(user);
    }

    @Test
    void lanzaExcepcionSiUsuarioNoExiste() {
        when(userRepository.findByUsername("usuario")).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> userService.findUserByUsername("usuario"));

        assertEquals("Usuario no encontrado: usuario", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());

        verify(userRepository).findByUsername("usuario");
        verify(userMapper, never()).toUserDto(any());
    }

    @Test
    void lanzaExcepcionPorUsuarioInexistente() {
        when(userRepository.findByUsername("usuario")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(credentialsDto.getUsername()));

        verify(userRepository).findByUsername("usuario");
        verify(passwordEncoder, never()).matches(any(), any());
        verify(userMapper, never()).toUserDto(any());
    }
}
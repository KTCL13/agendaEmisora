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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.nio.CharBuffer;

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
        user.setLogin("usuario");
        user.setPassword("hashedPassword");

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setLogin("usuario");

        credentialsDto = new CredentialsDto();
        credentialsDto.setLogin("usuario");
        credentialsDto.setPassword("password123".toCharArray());

        signUpDto = new SignUpDto();
        signUpDto.setLogin("nuevoUsuario");
        signUpDto.setPassword("newPass123");
    }

    @Test
    void encuentraUsuarioPorLogin() {
        when(userRepository.findByLogin("usuario")).thenReturn(Optional.of(user));
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        UserDto result = userService.findByLogin("usuario");

        assertNotNull(result);
        assertEquals(userDto.getLogin(), result.getLogin());

        verify(userRepository).findByLogin("usuario");
        verify(userMapper).toUserDto(user);
    }

    @Test
    void lanzaExcepcionSiUsuarioNoExiste() {
        when(userRepository.findByLogin("usuario")).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> userService.findByLogin("usuario"));

        assertEquals("Unknown user", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());

        verify(userRepository).findByLogin("usuario");
        verify(userMapper, never()).toUserDto(any());
    }

    @Test
    void iniciaSesion() {
        when(userRepository.findByLogin("usuario")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(CharBuffer.wrap("password123"), "hashedPassword")).thenReturn(true);
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        UserDto result = userService.login(credentialsDto);

        assertNotNull(result);
        assertEquals(userDto.getLogin(), result.getLogin());

        verify(userRepository).findByLogin("usuario");
        verify(passwordEncoder).matches(CharBuffer.wrap("password123"), "hashedPassword");
        verify(userMapper).toUserDto(user);
    }

    @Test
    void lanzaExcepcionPorUsuarioInexistente() {
        when(userRepository.findByLogin("usuario")).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> userService.login(credentialsDto));

        assertEquals("Unknown user", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());

        verify(userRepository).findByLogin("usuario");
        verify(passwordEncoder, never()).matches(any(), any());
        verify(userMapper, never()).toUserDto(any());
    }

    @Test
    void lanzaExcepcionPorPasswordIncorrecto() {
        when(userRepository.findByLogin("usuario")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(CharBuffer.wrap("password123"), "hashedPassword")).thenReturn(false);

        AppException exception = assertThrows(AppException.class, () -> userService.login(credentialsDto));

        assertEquals("Invalid password", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());

        verify(userRepository).findByLogin("usuario");
        verify(passwordEncoder).matches(CharBuffer.wrap("password123"), "hashedPassword");
        verify(userMapper, never()).toUserDto(any());
    }

    @Test
    void registraUsuario() {
        when(userRepository.findByLogin(signUpDto.getLogin())).thenReturn(Optional.empty());
        when(userMapper.signUpDtoToUser(signUpDto)).thenReturn(user);
        when(passwordEncoder.encode(signUpDto.getPassword())).thenReturn("hashedPass");
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        UserDto result = userService.register(signUpDto);

        assertNotNull(result);
        assertEquals(userDto.getLogin(), result.getLogin());
        assertEquals("hashedPass", user.getPassword());

        verify(userRepository).findByLogin("nuevoUsuario");
        verify(userMapper).signUpDtoToUser(signUpDto);
        verify(passwordEncoder).encode("newPass123");
        verify(userRepository).save(user);
        verify(userMapper).toUserDto(user);
    }

    @Test
    void noRegistraUsuarioExistente() {
        when(userRepository.findByLogin("nuevoUsuario")).thenReturn(Optional.of(user));

        AppException exception = assertThrows(AppException.class, () -> userService.register(signUpDto));

        assertEquals("User already exists", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());

        verify(userRepository).findByLogin("nuevoUsuario");
        verify(userMapper, never()).signUpDtoToUser(any());
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }
}
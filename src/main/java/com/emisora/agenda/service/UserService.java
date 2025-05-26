package com.emisora.agenda.service;

import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emisora.agenda.dto.SignUpDto;
import com.emisora.agenda.dto.UserDto;
import com.emisora.agenda.enums.EstadoPersona;
import com.emisora.agenda.exceptions.AppException;
import com.emisora.agenda.mapper.UserMapper;
import com.emisora.agenda.model.User;
import com.emisora.agenda.model.personas.Persona;
import com.emisora.agenda.repository.UserRepository;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {
    
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;
     private final PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(usernameOrEmail)
                .orElseGet(() -> userRepository.findByEmail(usernameOrEmail)
                        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + usernameOrEmail)));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isActive(), 
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                new ArrayList<>() // Lista de authorities/roles
        );
    }

    @Transactional(readOnly = true)
    public UserDto findUserByUsername(String username) {
        User user= userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("Usuario no encontrado: " + username, HttpStatus.NOT_FOUND));

        return userMapper.toUserDto(user);

    }


    @Transactional
    public UserDto registerNewUserAccount(SignUpDto signUpDto) throws RuntimeException {


        if (userRepository.existsByUsername(signUpDto.getUsername())) {
            throw new RuntimeException("Error: El nombre de usuario ya está en uso: " + signUpDto.getUsername());
        }


        if (userRepository.existsByEmail(signUpDto.getEmail())) {
            throw new RuntimeException("Error: El email ya está en uso: " + signUpDto.getEmail());
        }

        User user = new User();
        user.setUsername(signUpDto.getUsername());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword())); 
        user.setEmail(signUpDto.getEmail());
        user.setActive(true); 


        user.setRoles(signUpDto.getRoles());


        User savedUser = userRepository.save(user);


        return userMapper.toUserDto(savedUser);
    }

    public Page<UserDto> getAllUsers(int page, int size, String ordenarPor, String direccionOrden, String searchTerm) {
          
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direccionOrden), ordenarPor));
        Page<User> usersPage;

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            usersPage = userRepository.findByTerminoBusqueda(searchTerm, pageable);
        } else {
            usersPage = userRepository.findAll(EstadoPersona.ACTIVO, pageable); 
        }
         return usersPage.map(userMapper::toUserDto);

    }

    

}

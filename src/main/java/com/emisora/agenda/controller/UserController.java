package com.emisora.agenda.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.emisora.agenda.service.UserService;
import com.emisora.agenda.dto.UserDto;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;


    @GetMapping
    public ResponseEntity<Page<UserDto>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "username,asc") String[] sort,
            @RequestParam(required = false) String search) {
        
        String ordenarPor = sort[0];
        String direccionOrden = (sort.length > 1 && sort[1].equalsIgnoreCase("desc")) ? "desc" : "asc";
        
        Page<UserDto> userPage = userService.getAllUsers(
                page,
                size,
                ordenarPor,
                direccionOrden,
                search
        );
        
        return new ResponseEntity<>(userPage, HttpStatus.OK);
    }





}

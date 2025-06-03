package com.emisora.agenda.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.emisora.agenda.enums.CarreraEnum;

@RestController
@RequestMapping("/api/carreras")
public class CarreraController {

    @GetMapping
    public List<CarreraEnum> getAllCarreras() {
      return Arrays.asList(CarreraEnum.values());
   }

    
}

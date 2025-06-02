package com.emisora.agenda.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emisora.agenda.enums.FacultadEnum;

@RestController
@RequestMapping("/api/facultades")
public class FacultadController {

    @GetMapping
    public List<FacultadEnum> getAllFacultades() {
        return Arrays.asList(FacultadEnum.values());
    }
}

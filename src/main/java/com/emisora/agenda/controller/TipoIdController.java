package com.emisora.agenda.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emisora.agenda.enums.TipoId;

@RestController
@RequestMapping("/api/tipoId")
public class TipoIdController {

    @GetMapping
    public List<TipoId> getAllTipoId() {
        return Arrays.asList(TipoId.values());
    }

}

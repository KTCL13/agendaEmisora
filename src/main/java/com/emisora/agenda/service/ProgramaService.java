package com.emisora.agenda.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emisora.agenda.dto.ProgramaDTO;
import com.emisora.agenda.mapper.ProgramaMapper;
import com.emisora.agenda.model.Programa;
import com.emisora.agenda.repo.ProgramaRepository;

@Service
public class ProgramaService {

    @Autowired
    private ProgramaRepository programaRepository;

    public List<ProgramaDTO> getAllProgramas() {

        List<Programa> programas = programaRepository.findAll();
        
        return programas.stream()
                .map(ProgramaMapper.INSTANCE::toDto).collect(Collectors.toList());
    }

    public ProgramaDTO savePrograma(ProgramaDTO programaDTO) {
        Programa programa = programaRepository.save(ProgramaMapper.INSTANCE.toEntity(programaDTO));
        return ProgramaMapper.INSTANCE.toDto(programa);
    }

    
}

package com.emisora.agenda.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.emisora.agenda.dto.ProgramaDTO;
import com.emisora.agenda.mapper.ProgramaMapper;
import com.emisora.agenda.model.Programa;
import com.emisora.agenda.repository.ProgramaRepository;

@Service
public class ProgramaService {

    private final ProgramaRepository programaRepository;
    private final ProgramaMapper programaMapper;


    public ProgramaService(ProgramaRepository programaRepository, ProgramaMapper programaMapper) {
        this.programaRepository = programaRepository;
        this.programaMapper = programaMapper;
    }

    public List<ProgramaDTO> getAllProgramas() {
        List<Programa> programas = programaRepository.findAll();
        
        return programas.stream()
                .map(programaMapper::toDto).collect(Collectors.toList());
    }

    public ProgramaDTO savePrograma(ProgramaDTO programaDTO) {
        Programa programa = programaRepository.save(programaMapper.toEntity(programaDTO));
        return programaMapper.toDto(programa);
    }

    public void deletePrograma(Long id) {
        programaRepository.deleteById(id);
    }
}
package com.emisora.agenda.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;

import com.emisora.agenda.dto.ProgramaDTO;
import com.emisora.agenda.enums.EstadoEnum;
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

    public Page<ProgramaDTO> getAllProgramas(int page, int size, String ordenarPor, String direccionOrden, String searchTerm ) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direccionOrden), ordenarPor));
        Page<Programa> programasPage;

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            programasPage = programaRepository.findByTerminoBusqueda(EstadoEnum.ACTIVO, searchTerm, pageable);
        } else {
            programasPage = programaRepository.findByEstado(EstadoEnum.ACTIVO, pageable);
        }
        return programasPage.map(programaMapper::toDto);
    }

    public ProgramaDTO savePrograma(ProgramaDTO programaDTO) {
        Programa programa = programaMapper.toEntity(programaDTO);
        programa.setEstado(EstadoEnum.ACTIVO);
        programa = programaRepository.save(programa);
        return programaMapper.toDto(programa);
    }

    public ProgramaDTO updatePrograma(Long id, ProgramaDTO programaDTO) {
        if (!programaRepository.existsById(id)) {
            return null; 
        }
        programaDTO.setId(id);
        Programa programa = programaMapper.toEntity(programaDTO);
        programa = programaRepository.save(programa);
        return programaMapper.toDto(programa);
    }


    public void deletePrograma(Long id) {
       Programa programa = programaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Programa no encontrado con ID: " + id));
        programa.setEstado(EstadoEnum.INACTIVO);
        programaRepository.save(programa);
    }

    public ProgramaDTO getProgramaById(Long id) {
        return programaRepository.findById(id)
                .map(programaMapper::toDto)
                .orElse(null);
    }

    
}

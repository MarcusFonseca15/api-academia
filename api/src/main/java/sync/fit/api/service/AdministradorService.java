package sync.fit.api.service;

import sync.fit.api.dto.response.AdministradorResponseDTO;
import sync.fit.api.exception.ResourceNotFoundException;
import sync.fit.api.mapper.AdministradorMapper;
import sync.fit.api.model.Administrador;
import sync.fit.api.repository.AdministradorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdministradorService {

    private final AdministradorRepository administradorRepository;
    private final AdministradorMapper administradorMapper;

    @Transactional(readOnly = true)
    public List<AdministradorResponseDTO> findAllAdministradores() {
        return administradorRepository.findAll().stream()
                .map(administradorMapper::toResponseDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AdministradorResponseDTO findAdministradorById(Long id) {
        Administrador administrador = administradorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Administrador não encontrado com ID: " + id));
        return administradorMapper.toResponseDTO(administrador);
    }


}
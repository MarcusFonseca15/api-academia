package sync.fit.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sync.fit.api.dto.request.PlanoRequestDTO;
import sync.fit.api.dto.response.PlanoResponseDTO;
import sync.fit.api.exception.ResourceNotFoundException;
import sync.fit.api.model.Plano;
import sync.fit.api.repository.PlanoRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlanoService {

    @Autowired
    private PlanoRepository planoRepository;

    public PlanoResponseDTO criar(PlanoRequestDTO dto) {
        Plano plano = new Plano();
        plano.setTipo(dto.getTipo());
        plano.setValor(dto.getValor());
        plano.setDuracaoMeses(dto.getDuracaoMeses());

        Plano salvo = planoRepository.save(plano);

        return toResponseDTO(salvo);
    }

    public List<PlanoResponseDTO> listarTodos() {
        return planoRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true) // Apenas leitura
    public PlanoResponseDTO buscarPorId(Long id) {
        Plano plano = planoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plano não encontrado com ID: " + id));
        return toResponseDTO(plano);
    }


    @Transactional
    public PlanoResponseDTO atualizar(Long id, PlanoRequestDTO dto) {
        Plano existingPlano = planoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plano não encontrado com ID: " + id));

        
        existingPlano.setTipo(dto.getTipo());
        existingPlano.setValor(dto.getValor());
        existingPlano.setDuracaoMeses(dto.getDuracaoMeses());

        Plano atualizado = planoRepository.save(existingPlano);
        return toResponseDTO(atualizado);
    }

    private PlanoResponseDTO toResponseDTO(Plano plano) {
        PlanoResponseDTO dto = new PlanoResponseDTO();
        dto.setId(plano.getId());
        dto.setTipo(plano.getTipo());
        dto.setValor(plano.getValor());
        dto.setDuracaoMeses(plano.getDuracaoMeses());
        return dto;
    }

    @Transactional
    public void delete(Long id) {
        if (!planoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Plano não encontrado com ID: " + id);
        }
        planoRepository.deleteById(id);
    }
}
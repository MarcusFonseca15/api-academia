package sync.fit.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sync.fit.api.dto.PlanoRequestDTO;
import sync.fit.api.dto.PlanoResponseDTO;
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

    private PlanoResponseDTO toResponseDTO(Plano plano) {
        PlanoResponseDTO dto = new PlanoResponseDTO();
        dto.setId(plano.getId());
        dto.setTipo(plano.getTipo());
        dto.setValor(plano.getValor());
        dto.setDuracaoMeses(plano.getDuracaoMeses());
        return dto;
    }
}

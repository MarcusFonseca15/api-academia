package sync.fit.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sync.fit.api.dto.request.ExercicioRequestDTO;
import sync.fit.api.dto.response.ExercicioResponseDTO;
import sync.fit.api.model.Exercicio;
import sync.fit.api.model.Treino;
import sync.fit.api.repository.ExercicioRepository;
import sync.fit.api.repository.TreinoRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExercicioService {

    @Autowired
    private ExercicioRepository exercicioRepository;

    @Autowired
    private TreinoRepository treinoRepository;

    public ExercicioResponseDTO criar(ExercicioRequestDTO dto) {
        Treino treino = treinoRepository.findById(dto.getTreinoId()).orElseThrow();

        Exercicio e = new Exercicio();
        e.setNome(dto.getNome());
        e.setDescricao(dto.getDescricao());
        e.setRepeticoes(dto.getRepeticoes());
        e.setSeries(dto.getSeries());
        e.setTreino(treino);

        Exercicio salvo = exercicioRepository.save(e);

        return toResponseDTO(salvo);
    }

    public List<ExercicioResponseDTO> listarTodos() {
        return exercicioRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    private ExercicioResponseDTO toResponseDTO(Exercicio e) {
        ExercicioResponseDTO dto = new ExercicioResponseDTO();
        dto.setId(e.getId());
        dto.setNome(e.getNome());
        dto.setDescricao(e.getDescricao());
        dto.setRepeticoes(e.getRepeticoes());
        dto.setSeries(e.getSeries());
        dto.setNomeTreino(e.getTreino().getDescricao());
        return dto;
    }

    public ExercicioResponseDTO update(Long id, ExercicioRequestDTO dto) {
        Exercicio e = exercicioRepository.findById(id).orElseThrow();

        e.setNome(dto.getNome());
        e.setDescricao(dto.getDescricao());
        e.setRepeticoes(dto.getRepeticoes());
        e.setSeries(dto.getSeries());

        Treino treino = treinoRepository.findById(dto.getTreinoId()).orElseThrow();
        e.setTreino(treino);

        Exercicio atualizado = exercicioRepository.save(e);
        return toResponseDTO(atualizado);
    }

    public void delete(Long id) {
        Exercicio e = exercicioRepository.findById(id).orElseThrow();
        exercicioRepository.delete(e);
    }

}

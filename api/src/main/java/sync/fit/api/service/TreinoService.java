package sync.fit.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sync.fit.api.dto.request.TreinoRequestDTO;
import sync.fit.api.dto.response.ExercicioSimplesDTO;
import sync.fit.api.dto.response.TreinoResponseDTO;
import sync.fit.api.model.Cliente;
import sync.fit.api.model.Treino;
import sync.fit.api.repository.ClienteRepository;
import sync.fit.api.repository.TreinoRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TreinoService {

    @Autowired
    private TreinoRepository treinoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    public TreinoResponseDTO criar(TreinoRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId()).orElseThrow();

        Treino treino = new Treino();
        treino.setDescricao(dto.getDescricao());
        treino.setCliente(cliente);

        Treino salvo = treinoRepository.save(treino);
        return toResponseDTO(salvo);
    }

    public List<TreinoResponseDTO> listarTodos() {
        return treinoRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    private TreinoResponseDTO toResponseDTO(Treino treino) {
        TreinoResponseDTO dto = new TreinoResponseDTO();
        dto.setId(treino.getId());
        dto.setDescricao(treino.getDescricao());
        dto.setNomeCliente(treino.getCliente().getNome());

        List<ExercicioSimplesDTO> exercicios = treino.getExercicios().stream().map(e -> {
            ExercicioSimplesDTO ex = new ExercicioSimplesDTO();
            ex.setId(e.getId());
            ex.setNome(e.getNome());
            ex.setRepeticoes(e.getRepeticoes());
            ex.setSeries(e.getSeries());
            return ex;
        }).toList();

        dto.setExercicios(exercicios);
        return dto;
    }
    public TreinoResponseDTO atualizarTreino(Long id, TreinoRequestDTO dto) {
        Treino treino = treinoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("vai ficar sem treinar"));

        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("sem clientes, cara"));

        treino.setDescricao(dto.getDescricao());
        treino.setCliente(cliente);

        Treino atualizado = treinoRepository.save(treino);
        return toResponseDTO(atualizado);
    }

    public void deletarTreino(Long id) {
        Treino treino = treinoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sem treino amigo kkkk"));

        treinoRepository.delete(treino);
    }
}

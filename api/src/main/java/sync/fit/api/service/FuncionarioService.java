package sync.fit.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sync.fit.api.dto.request.FuncionarioRequestDTO;
import sync.fit.api.dto.response.FuncionarioResponseDTO;
import sync.fit.api.model.Funcionario;
import sync.fit.api.repository.FuncionarioRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    public FuncionarioResponseDTO criar(FuncionarioRequestDTO dto) {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome(dto.getNome());
        funcionario.setEmail(dto.getEmail());
        funcionario.setCargo(dto.getCargo());
        funcionario.setSalario(dto.getSalario());

        Funcionario salvo = funcionarioRepository.save(funcionario);
        return toResponseDTO(salvo);
    }

    public List<FuncionarioResponseDTO> listarTodos() {
        return funcionarioRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    private FuncionarioResponseDTO toResponseDTO(Funcionario f) {
        FuncionarioResponseDTO dto = new FuncionarioResponseDTO();
        dto.setId(f.getId());
        dto.setNome(f.getNome());
        dto.setEmail(f.getEmail());
        dto.setCargo(f.getCargo());
        dto.setSalario(f.getSalario());
        return dto;
    }
}

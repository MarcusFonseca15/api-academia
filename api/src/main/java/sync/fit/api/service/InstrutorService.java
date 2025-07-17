package sync.fit.api.service;

import sync.fit.api.dto.response.ClienteResponseDTO;
import sync.fit.api.dto.response.InstrutorResponseDTO;
import sync.fit.api.exception.ResourceNotFoundException;
import sync.fit.api.mapper.InstrutorMapper;
import sync.fit.api.model.Instrutor;
import sync.fit.api.repository.InstrutorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstrutorService {

    private final InstrutorRepository instrutorRepository;
    private final InstrutorMapper instrutorMapper;

    @Transactional(readOnly = true)
    public InstrutorResponseDTO findById(Long id) {
        Instrutor instrutor = instrutorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instrutor não encontrado com ID: " + id));
        return instrutorMapper.toResponseDTO(instrutor);
    }

    @Transactional(readOnly = true)
    public List<InstrutorResponseDTO> findAll() {
        return instrutorRepository.findAll().stream()
                .map(instrutorMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> findAlunosByInstrutorId(Long instrutorId) {
        Instrutor instrutor = instrutorRepository.findById(instrutorId)
                .orElseThrow(() -> new ResourceNotFoundException("Instrutor não encontrado com ID: " + instrutorId));

        // Mapeia a coleção diretamente usando o ClienteMapper via InstrutorMapper

        return instrutorMapper.toClienteResponseDTOList(instrutor.getAlunos());
    }
}
package sync.fit.api.service;

import sync.fit.api.dto.request.ClienteAdminUpdateDTO; // Importe o DTO renomeado
import sync.fit.api.dto.request.ClienteUpdateProfileDTO; // Importe o novo DTO
import sync.fit.api.dto.request.ClienteRegisterRequestDTO;
import sync.fit.api.dto.response.ClienteResponseDTO;
import sync.fit.api.exception.ResourceNotFoundException;
import sync.fit.api.mapper.ClienteMapper;
import sync.fit.api.model.Cliente;
import sync.fit.api.model.Plano;
import sync.fit.api.model.Role;
import sync.fit.api.model.Instrutor;
import sync.fit.api.repository.ClienteRepository;
import sync.fit.api.repository.PlanoRepository;
import sync.fit.api.repository.RoleRepository;
import sync.fit.api.repository.InstrutorRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final PlanoRepository planoRepository;
    private final InstrutorRepository instrutorRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ClienteMapper clienteMapper;

    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> findAll() {
        return clienteRepository.findAll().stream()
                .map(clienteMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ClienteResponseDTO findById(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com ID: " + id));
        return clienteMapper.toResponseDTO(cliente);
    }

    @Transactional
    public ClienteResponseDTO save(ClienteRegisterRequestDTO requestDTO) {
        Plano plano = null;
        if (requestDTO.getPlanoId() != null) {
            plano = planoRepository.findById(requestDTO.getPlanoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Plano não encontrado com ID: " + requestDTO.getPlanoId()));
        }

        Instrutor instrutor = null;
        if (requestDTO.getInstrutorId() != null) {
            instrutor = instrutorRepository.findById(requestDTO.getInstrutorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Instrutor não encontrado com ID: " + requestDTO.getInstrutorId()));
        }

        if (clienteRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado.");
        }

        Cliente cliente = new Cliente(
                requestDTO.getNome(),
                requestDTO.getEmail(),
                passwordEncoder.encode(requestDTO.getSenha()),
                requestDTO.getDataNascimento(),
                requestDTO.getTelefone(),
                plano,
                instrutor
        );

        cliente.setPlano(plano);
        cliente.setInstrutor(instrutor);

        Role clienteRole = roleRepository.findByName("ROLE_CLIENTE")
                .orElseThrow(() -> new ResourceNotFoundException("Role 'ROLE_CLIENTE' não encontrada. Certifique-se de que ela existe no banco de dados."));
        cliente.getRoles().add(clienteRole);

        Cliente salvo = clienteRepository.save(cliente);
        return clienteMapper.toResponseDTO(salvo);
    }

    // NOVO MÉTODO: Atualização de perfil pelo CLIENTE
    @Transactional
    public ClienteResponseDTO updateProfile(Long id, ClienteUpdateProfileDTO requestDTO) {
        Cliente existingCliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com ID: " + id));

        if (!existingCliente.getEmail().equals(requestDTO.getEmail())
                && clienteRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado para outro cliente.");
        }

        existingCliente.setNome(requestDTO.getNome());
        existingCliente.setEmail(requestDTO.getEmail());
        existingCliente.setTelefone(requestDTO.getTelefone());
        existingCliente.setDataNascimento(requestDTO.getDataNascimento());



        if (requestDTO.getSenha() != null && !requestDTO.getSenha().isBlank()) {
            existingCliente.setSenha(passwordEncoder.encode(requestDTO.getSenha()));
        }

        Cliente atualizado = clienteRepository.save(existingCliente);
        return clienteMapper.toResponseDTO(atualizado);
    }

    // MÉTODO EXISTENTE, AGORA PARA ATUALIZAÇÃO PELO ADMINISTRADOR
    @Transactional
    public ClienteResponseDTO updateByAdmin(Long id, ClienteAdminUpdateDTO requestDTO) {
        Cliente existingCliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com ID: " + id));

        if (!existingCliente.getEmail().equals(requestDTO.getEmail())
                && clienteRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado para outro cliente.");
        }

        existingCliente.setNome(requestDTO.getNome());
        existingCliente.setEmail(requestDTO.getEmail());
        existingCliente.setTelefone(requestDTO.getTelefone());
        existingCliente.setDataNascimento(requestDTO.getDataNascimento());

        // Lógica para atualização de Plano pelo ADMIN
        if (requestDTO.getPlanoId() != null) {
            Plano novoPlano = planoRepository.findById(requestDTO.getPlanoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Plano não encontrado com ID: " + requestDTO.getPlanoId()));
            existingCliente.setPlano(novoPlano);
        } else {
            existingCliente.setPlano(null); // Permite remover o plano pelo ADMIN
        }

        // Lógica para atualização de Instrutor pelo ADMIN
        if (requestDTO.getInstrutorId() != null) {
            Instrutor novoInstrutor = instrutorRepository.findById(requestDTO.getInstrutorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Instrutor não encontrado com ID: " + requestDTO.getInstrutorId()));
            existingCliente.setInstrutor(novoInstrutor);
        } else {
            existingCliente.setInstrutor(null); // Permite remover o instrutor pelo ADMIN
        }

        if (requestDTO.getSenha() != null && !requestDTO.getSenha().isBlank()) {
            existingCliente.setSenha(passwordEncoder.encode(requestDTO.getSenha()));
        }

        Cliente atualizado = clienteRepository.save(existingCliente);
        return clienteMapper.toResponseDTO(atualizado);
    }

    @Transactional
    public void delete(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente não encontrado com ID: " + id);
        }
        clienteRepository.deleteById(id);
    }
}
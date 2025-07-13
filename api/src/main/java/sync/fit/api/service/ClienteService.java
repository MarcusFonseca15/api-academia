// sync.fit.api.service.ClienteService.java
package sync.fit.api.service;

import sync.fit.api.dto.request.ClienteRequestDTO;
import sync.fit.api.dto.request.ClienteRegisterRequestDTO;
import sync.fit.api.dto.response.ClienteResponseDTO;
import sync.fit.api.exception.ResourceNotFoundException;
import sync.fit.api.mapper.ClienteMapper;
import sync.fit.api.model.Administrador;
import sync.fit.api.model.Cliente;
import sync.fit.api.model.Plano;
import sync.fit.api.model.Role;
import sync.fit.api.model.Instrutor; // Importar Instrutor
import sync.fit.api.repository.AdministradorRepository;
import sync.fit.api.repository.ClienteRepository;
import sync.fit.api.repository.PlanoRepository;
import sync.fit.api.repository.RoleRepository;
import sync.fit.api.repository.InstrutorRepository; // Importar InstrutorRepository
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final PlanoRepository planoRepository;
    private final AdministradorRepository administradorRepository;
    private final InstrutorRepository instrutorRepository; // INJETADO
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ClienteMapper clienteMapper;

    // Remova o @Autowired aqui e confie no @RequiredArgsConstructor
    // private final EmailService emailService; // Se você tiver um EmailService, mantenha-o ou remova conforme necessário

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
        Administrador administrador = administradorRepository.findById(requestDTO.getAdministradorId())
                .orElseThrow(() -> new ResourceNotFoundException("Administrador não encontrado com ID: " + requestDTO.getAdministradorId()));

        Plano plano = planoRepository.findById(requestDTO.getPlanoId())
                .orElseThrow(() -> new ResourceNotFoundException("Plano não encontrado com ID: " + requestDTO.getPlanoId()));

        Instrutor instrutor = instrutorRepository.findById(requestDTO.getInstrutorId()) // BUSCAR INSTRUTOR
                .orElseThrow(() -> new ResourceNotFoundException("Instrutor não encontrado com ID: " + requestDTO.getInstrutorId()));

        if (clienteRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado.");
        }

        Cliente cliente = clienteMapper.toEntity(requestDTO);
        cliente.setSenha(passwordEncoder.encode(requestDTO.getSenha()));
        cliente.setPlano(plano);
        cliente.setAdministrador(administrador);
        cliente.setInstrutor(instrutor); // ATRIBUIR INSTRUTOR

        Role clienteRole = roleRepository.findByName("ROLE_CLIENTE")
                .orElseThrow(() -> new ResourceNotFoundException("Role 'ROLE_CLIENTE' não encontrada. Certifique-se de que ela existe no banco de dados."));
        cliente.getRoles().add(clienteRole);

        Cliente salvo = clienteRepository.save(cliente);
        // emailService.sendEmail(salvo.getEmail(), "Bem-vindo ao sistema!", "Seu cadastro foi realizado com sucesso.");
        return clienteMapper.toResponseDTO(salvo);
    }

    @Transactional
    public ClienteResponseDTO update(Long id, ClienteRequestDTO requestDTO) {
        Cliente existingCliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com ID: " + id));

        if (!existingCliente.getEmail().equals(requestDTO.getEmail())
                && clienteRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado para outro cliente.");
        }

        existingCliente.setNome(requestDTO.getNome());
        existingCliente.setEmail(requestDTO.getEmail());
        existingCliente.setTelefone(requestDTO.getTelefone());

        if (requestDTO.getPlanoId() != null && !existingCliente.getPlano().getId().equals(requestDTO.getPlanoId())) {
            Plano novoPlano = planoRepository.findById(requestDTO.getPlanoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Plano não encontrado com ID: " + requestDTO.getPlanoId()));
            existingCliente.setPlano(novoPlano);
        }

        if (requestDTO.getAdministradorId() != null
                && !existingCliente.getAdministrador().getId().equals(requestDTO.getAdministradorId())) {
            Administrador novoAdmin = administradorRepository.findById(requestDTO.getAdministradorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Administrador não encontrado com ID: " + requestDTO.getAdministradorId()));
            existingCliente.setAdministrador(novoAdmin);
        }

        // NOVO: Atualiza o instrutor se o ID for diferente
        if (requestDTO.getInstrutorId() != null &&
                (existingCliente.getInstrutor() == null || !existingCliente.getInstrutor().getId().equals(requestDTO.getInstrutorId()))) {
            Instrutor novoInstrutor = instrutorRepository.findById(requestDTO.getInstrutorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Instrutor não encontrado com ID: " + requestDTO.getInstrutorId()));
            existingCliente.setInstrutor(novoInstrutor);
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

    // Remova este método toResponseDTO manual, pois você está usando o MapStruct
    // private ClienteResponseDTO toResponseDTO(Cliente cliente) { ... }
}
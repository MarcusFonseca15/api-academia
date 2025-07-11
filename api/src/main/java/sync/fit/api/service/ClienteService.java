package sync.fit.api.service;

import sync.fit.api.dto.request.ClienteRequestDTO;
import sync.fit.api.dto.request.ClienteRegisterRequestDTO;
import sync.fit.api.dto.response.ClienteResponseDTO;
import sync.fit.api.exception.ResourceNotFoundException;
import sync.fit.api.mapper.ClienteMapper; // IMPORTAR O MAPPER
import sync.fit.api.model.Administrador;
import sync.fit.api.model.Cliente;
import sync.fit.api.model.Plano;
import sync.fit.api.model.Role; // IMPORTAR A ENTIDADE ROLE
import sync.fit.api.repository.AdministradorRepository;
import sync.fit.api.repository.ClienteRepository;
import sync.fit.api.repository.PlanoRepository;
import sync.fit.api.repository.RoleRepository; // IMPORTAR O ROLE REPOSITORY
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
<<<<<<< HEAD
import org.springframework.transaction.annotation.Transactional; // Para operações transacionais
import sync.fit.api.service.EmailService;
=======
import org.springframework.transaction.annotation.Transactional;
>>>>>>> 9b41120bacc682f74f439a302b1aecc69e9baf81

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final PlanoRepository planoRepository;
    private final AdministradorRepository administradorRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository; // INJETAR ROLE REPOSITORY
    private final ClienteMapper clienteMapper; // INJETAR CLIENTE MAPPER

    @Autowired
    private EmailService emailService;

    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> findAll() {
        return clienteRepository.findAll().stream()
                .map(clienteMapper::toResponseDTO) // USAR O MAPPER
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ClienteResponseDTO findById(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com ID: " + id));
        return clienteMapper.toResponseDTO(cliente); // USAR O MAPPER
    }

    @Transactional
    public ClienteResponseDTO save(ClienteRegisterRequestDTO requestDTO) { // Usa ClienteRegisterRequestDTO
        Administrador administrador = administradorRepository.findById(requestDTO.getAdministradorId())
                .orElseThrow(() -> new ResourceNotFoundException("Administrador não encontrado com ID: " + requestDTO.getAdministradorId()));

        Plano plano = planoRepository.findById(requestDTO.getPlanoId())
                .orElseThrow(() -> new ResourceNotFoundException("Plano não encontrado com ID: " + requestDTO.getPlanoId()));

        if (clienteRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado.");
        }

        // Mapeia o DTO para a entidade Cliente. A senha será criptografada depois.
        Cliente cliente = clienteMapper.toEntity(requestDTO);

        // A senha do DTO é criptografada e atribuída ao objeto Cliente
        cliente.setSenha(passwordEncoder.encode(requestDTO.getSenha()));

        cliente.setPlano(plano); // Atribui o objeto Plano completo
        cliente.setAdministrador(administrador); // Atribui o objeto Administrador completo

        // Atribuir a role 'ROLE_CLIENTE' ao novo cliente
        Role clienteRole = roleRepository.findByName("ROLE_CLIENTE")
                .orElseThrow(() -> new ResourceNotFoundException("Role 'ROLE_CLIENTE' não encontrada. Certifique-se de que ela existe no banco de dados."));
        cliente.getRoles().add(clienteRole);

        Cliente salvo = clienteRepository.save(cliente);
        return clienteMapper.toResponseDTO(salvo); // USAR O MAPPER
    }

    @Transactional
    public ClienteResponseDTO update(Long id, ClienteRequestDTO requestDTO) {
        Cliente existingCliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com ID: " + id));

        if (!existingCliente.getEmail().equals(requestDTO.getEmail()) && clienteRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado para outro cliente.");
        }

        // Mapeia os campos atualizáveis do DTO para a entidade existente
        // IMPORTANTE: o mapper `toEntity` para ClienteRequestDTO ignora o ID e a senha (no DTO de RequestDTO)
        // então precisamos copiar manualmente para os campos que não queremos ignorar
        existingCliente.setNome(requestDTO.getNome());
        existingCliente.setEmail(requestDTO.getEmail());
        existingCliente.setTelefone(requestDTO.getTelefone());


        if (requestDTO.getPlanoId() != null && !existingCliente.getPlano().getId().equals(requestDTO.getPlanoId())) {
            Plano novoPlano = planoRepository.findById(requestDTO.getPlanoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Plano não encontrado com ID: " + requestDTO.getPlanoId()));
            existingCliente.setPlano(novoPlano);
        }

        if (requestDTO.getAdministradorId() != null && !existingCliente.getAdministrador().getId().equals(requestDTO.getAdministradorId())) {
            Administrador novoAdmin = administradorRepository.findById(requestDTO.getAdministradorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Administrador não encontrado com ID: " + requestDTO.getAdministradorId()));
            existingCliente.setAdministrador(novoAdmin);
        }

        if (requestDTO.getSenha() != null && !requestDTO.getSenha().isBlank()) {
            existingCliente.setSenha(passwordEncoder.encode(requestDTO.getSenha()));
        }

        Cliente atualizado = clienteRepository.save(existingCliente);
        return clienteMapper.toResponseDTO(atualizado); // USAR O MAPPER
    }

    @Transactional
    public void delete(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente não encontrado com ID: " + id);
        }
        clienteRepository.deleteById(id);
    }
<<<<<<< HEAD

    private ClienteResponseDTO toResponseDTO(Cliente cliente) {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(cliente.getId());
        dto.setNome(cliente.getNome());
        dto.setEmail(cliente.getEmail());
        dto.setTelefone(cliente.getTelefone());
        dto.setPlanoTipo(cliente.getPlano().getTipo());
//        dto.setPlanoId(cliente.getPlano().getId()); // Adicionado
        dto.setAdministradorNome(cliente.getAdministrador().getNome());
//        dto.setAdministradorId(cliente.getAdministrador().getId()); // Adicionado
        return dto;
    }

    emailService.enviarEmail(
            cliente.getEmail(),
            "Vencimento de Plano",
            "Olá " + cliente.getNome() + ",\n\nSeu plano está prestes a vencer. Por favor, renove seu plano para continuar usufruindo dos nossos serviços."
    );
=======
>>>>>>> 9b41120bacc682f74f439a302b1aecc69e9baf81
}
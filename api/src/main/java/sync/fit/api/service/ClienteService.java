package sync.fit.api.service;


import sync.fit.api.dto.request.ClienteRequestDTO;
import sync.fit.api.dto.response.ClienteResponseDTO;
import sync.fit.api.model.Administrador;
import sync.fit.api.model.Cliente;
import sync.fit.api.model.Plano;
import sync.fit.api.repository.AdministradorRepository;
import sync.fit.api.repository.ClienteRepository;
import sync.fit.api.repository.PlanoRepository;
import sync.fit.api.exception.ResourceNotFoundException; // Precisaremos criar esta exceção
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Para operações transacionais
import sync.fit.api.service.EmailService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PlanoRepository planoRepository;

    @Autowired
    private AdministradorRepository administradorRepository;

    @Autowired
    private EmailService emailService;

    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> findAll() {
        return clienteRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ClienteResponseDTO findById(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com ID: " + id));
        return toResponseDTO(cliente);
    }

    @Transactional
    public ClienteResponseDTO save(ClienteRequestDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNome(dto.getNome());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefone(dto.getTelefone());

        Plano plano = planoRepository.findById(dto.getPlanoId())
                .orElseThrow(() -> new ResourceNotFoundException("Plano não encontrado com ID: " + dto.getPlanoId()));
        Administrador admin = administradorRepository.findById(dto.getAdministradorId())
                .orElseThrow(() -> new ResourceNotFoundException("Administrador não encontrado com ID: " + dto.getAdministradorId()));

        cliente.setPlano(plano);
        cliente.setAdministrador(admin);

        Cliente salvo = clienteRepository.save(cliente);
        return toResponseDTO(salvo);
    }

    @Transactional
    public ClienteResponseDTO update(Long id, ClienteRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com ID: " + id));

        cliente.setNome(dto.getNome());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefone(dto.getTelefone());

        // Atualiza Plano e Administrador apenas se os IDs forem diferentes
        if (!cliente.getPlano().getId().equals(dto.getPlanoId())) {
            Plano novoPlano = planoRepository.findById(dto.getPlanoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Plano não encontrado com ID: " + dto.getPlanoId()));
            cliente.setPlano(novoPlano);
        }

        if (!cliente.getAdministrador().getId().equals(dto.getAdministradorId())) {
            Administrador novoAdmin = administradorRepository.findById(dto.getAdministradorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Administrador não encontrado com ID: " + dto.getAdministradorId()));
            cliente.setAdministrador(novoAdmin);
        }

        Cliente atualizado = clienteRepository.save(cliente);
        return toResponseDTO(atualizado);
    }

    @Transactional
    public void delete(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente não encontrado com ID: " + id);
        }
        clienteRepository.deleteById(id);
    }

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
}
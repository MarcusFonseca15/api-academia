package sync.fit.api.service;

//import sync.fit.api.dto.ClienteResponseDTO;

// import com.seuprojeto.dto.ClienteRequestDTO;
// import com.seuprojeto.dto.ClienteResponseDTO;
import sync.fit.api.dto.request.ClienteRequestDTO;
import sync.fit.api.dto.response.ClienteResponseDTO;
import sync.fit.api.model.Administrador;
import sync.fit.api.model.Cliente;
import sync.fit.api.model.Plano;
import sync.fit.api.repository.AdministradorRepository;
import sync.fit.api.repository.ClienteRepository;
import sync.fit.api.repository.PlanoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public ClienteResponseDTO criar(ClienteRequestDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNome(dto.getNome());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefone(dto.getTelefone());

        Plano plano = planoRepository.findById(dto.getPlanoId())
                .orElseThrow(() -> new RuntimeException("Plano não encontrado"));
        Administrador admin = administradorRepository.findById(dto.getAdministradorId())
                .orElseThrow(() -> new RuntimeException("Administrador não encontrado"));

        cliente.setPlano(plano);
        cliente.setAdministrador(admin);

        Cliente salvo = clienteRepository.save(cliente);

        return toResponseDTO(salvo);
    }

    public List<ClienteResponseDTO> listarTodos() {
        return clienteRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    private ClienteResponseDTO toResponseDTO(Cliente cliente) {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(cliente.getId());
        dto.setNome(cliente.getNome());
        dto.setEmail(cliente.getEmail());
        dto.setTelefone(cliente.getTelefone());
        dto.setPlanoTipo(cliente.getPlano().getTipo());
        dto.setAdministradorNome(cliente.getAdministrador().getNome());
        return dto;
    }
}

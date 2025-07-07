package sync.fit.api.service;


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

    public List<ClienteResponseDTO> findAll() {
    }

    public ClienteResponseDTO findById(Long id) {
    }

    public ClienteResponseDTO save(ClienteRequestDTO requestDTO) {
    }

    public ClienteResponseDTO update(Long id, ClienteRequestDTO requestDTO) {
    }

    public void delete(Long id) {
    }
}

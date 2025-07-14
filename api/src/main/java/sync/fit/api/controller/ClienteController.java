// sync.fit.api.controller.ClienteController.java
package sync.fit.api.controller;

import sync.fit.api.dto.request.ClienteRequestDTO;
import sync.fit.api.dto.request.ClienteRegisterRequestDTO;
import sync.fit.api.dto.response.ClienteResponseDTO;
import sync.fit.api.model.Cliente;
import sync.fit.api.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> getAllClientes() {
        List<ClienteResponseDTO> clientes = clienteService.findAll();
        return ResponseEntity.ok(clientes);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUTOR', 'RECEPCIONISTA') or (hasRole('CLIENTE') and #id == authentication.principal.id)")
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> getClienteById(@PathVariable Long id) {
        ClienteResponseDTO cliente = clienteService.findById(id);
        return ResponseEntity.ok(cliente);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('RECEPCIONISTA')")
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> createCliente(@Valid @RequestBody ClienteRegisterRequestDTO requestDTO) {
        ClienteResponseDTO cliente = clienteService.save(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> updateCliente(
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequestDTO requestDTO) {
        ClienteResponseDTO cliente = clienteService.update(id, requestDTO);
        return ResponseEntity.ok(cliente);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id) {
        clienteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('CLIENTE')")
    @GetMapping("/meu-perfil")
    public ResponseEntity<ClienteResponseDTO> getMeuPerfil() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // O principal aqui já deve ser a sua entidade Cliente se CustomUserDetailsService estiver configurado corretamente
        // para retornar um Cliente (que implementa UserDetails).
        Cliente clienteLogado = (Cliente) authentication.getPrincipal();

        // O serviço já busca por ID e mapeia para DTO
        ClienteResponseDTO meuPerfil = clienteService.findById(clienteLogado.getId());
        return ResponseEntity.ok(meuPerfil);
    }

    @PreAuthorize("hasRole('CLIENTE')") // Não precisa do #id == authentication.principal.id se o cliente só atualiza seu próprio perfil
    @PutMapping("/meu-perfil") // Removido {id} da URL para evitar confusão. O ID é do usuário logado.
    public ResponseEntity<ClienteResponseDTO> updateMeuPerfil(@Valid @RequestBody ClienteRequestDTO requestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Cliente clienteLogado = (Cliente) authentication.getPrincipal();

        // O service receberá o ID do cliente logado e o DTO para atualização.
        ClienteResponseDTO updatedCliente = clienteService.update(clienteLogado.getId(), requestDTO);
        return ResponseEntity.ok(updatedCliente);
    }
}
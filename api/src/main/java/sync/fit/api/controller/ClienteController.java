package sync.fit.api.controller;

import sync.fit.api.dto.request.ClienteRequestDTO;
import sync.fit.api.dto.request.ClienteRegisterRequestDTO; // Importar
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

    // Apenas ADMIN pode listar todos os clientes
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> getAllClientes() {
        List<ClienteResponseDTO> clientes = clienteService.findAll();
        return ResponseEntity.ok(clientes);
    }

    // Permite ADMIN, INSTRUTOR e RECEPCIONISTA ver qualquer cliente.
    // Permite CLIENTE ver APENAS seus próprios dados.
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUTOR', 'RECEPCIONISTA') or (hasRole('CLIENTE') and #id == authentication.principal.id)")
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> getClienteById(@PathVariable Long id) {
        ClienteResponseDTO cliente = clienteService.findById(id);
        return ResponseEntity.ok(cliente);
    }

    // Apenas ADMIN ou RECEPCIONISTA pode criar novos clientes
    @PreAuthorize("hasRole('ADMIN') or hasRole('RECEPCIONISTA')")
    @PostMapping // Usa ClienteRegisterRequestDTO para criação (com senha obrigatória)
    public ResponseEntity<ClienteResponseDTO> createCliente(@Valid @RequestBody ClienteRegisterRequestDTO requestDTO) {
        ClienteResponseDTO cliente = clienteService.save(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
    }

    // Apenas ADMIN pode atualizar qualquer cliente
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}") // Usa ClienteRequestDTO para atualização (senha opcional)
    public ResponseEntity<ClienteResponseDTO> updateCliente(
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequestDTO requestDTO) {
        ClienteResponseDTO cliente = clienteService.update(id, requestDTO);
        return ResponseEntity.ok(cliente);
    }

    // Apenas ADMIN pode deletar clientes
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id) {
        clienteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // NOVO ENDPOINT: Cliente pode ver seus próprios dados sem usar ID na URL
    @PreAuthorize("hasRole('CLIENTE')")
    @GetMapping("/meu-perfil")
    public ResponseEntity<ClienteResponseDTO> getMeuPerfil() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Cliente clienteLogado = (Cliente) authentication.getPrincipal();

        ClienteResponseDTO meuPerfil = clienteService.findById(clienteLogado.getId());
        return ResponseEntity.ok(meuPerfil);
    }

    // NOVO ENDPOINT: Cliente pode atualizar seus próprios dados
    @PreAuthorize("hasRole('CLIENTE') and #id == authentication.principal.id")
    @PutMapping("/meu-perfil/{id}")
    public ResponseEntity<ClienteResponseDTO> updateMeuPerfil(
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequestDTO requestDTO) {
        ClienteResponseDTO updatedCliente = clienteService.update(id, requestDTO);
        return ResponseEntity.ok(updatedCliente);
    }
}
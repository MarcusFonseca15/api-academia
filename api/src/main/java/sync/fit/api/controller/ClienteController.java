package sync.fit.api.controller;



import sync.fit.api.dto.request.ClienteRequestDTO;
import sync.fit.api.dto.response.ClienteResponseDTO;
import sync.fit.api.model.Cliente; // Importar a entidade Cliente
import sync.fit.api.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Importar
import org.springframework.security.core.Authentication; // Importar
import org.springframework.security.core.context.SecurityContextHolder; // Importar
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
        // A lógica de autorização já é tratada pelo @PreAuthorize.
        // Se a validação passar, o serviço pode ser chamado.
        ClienteResponseDTO cliente = clienteService.findById(id);
        return ResponseEntity.ok(cliente);
    }

    // Apenas ADMIN pode criar novos clientes (geralmente via recepção ou admin)
    // Se o cliente pode se registrar sozinho, este endpoint seria público (no /api/auth/**)
    @PreAuthorize("hasRole('ADMIN') or hasRole('RECEPCIONISTA')") // Ajuste conforme quem pode criar clientes
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> createCliente(@Valid @RequestBody ClienteRequestDTO requestDTO) {
        ClienteResponseDTO cliente = clienteService.save(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
    }

    // Apenas ADMIN pode atualizar qualquer cliente
    // O cliente pode atualizar seus próprios dados (veja o método updateMeuPerfil abaixo)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
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
        // O 'principal' é o objeto que você retorna em CustomUserDetailsService.loadUserByUsername()
        // No seu caso, é a entidade Cliente, pois Cliente implementa UserDetails.
        Cliente clienteLogado = (Cliente) authentication.getPrincipal();

        // Use o ID do cliente logado para buscar os dados completos
        ClienteResponseDTO meuPerfil = clienteService.findById(clienteLogado.getId());
        return ResponseEntity.ok(meuPerfil);
    }

    // NOVO ENDPOINT: Cliente pode atualizar seus próprios dados
    @PreAuthorize("hasRole('CLIENTE') and #id == authentication.principal.id")
    @PutMapping("/meu-perfil/{id}") // Ou simplesmente "/meu-perfil" se você não quiser o ID na URL
    public ResponseEntity<ClienteResponseDTO> updateMeuPerfil(
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequestDTO requestDTO) {
        // A lógica de autorização (#id == authentication.principal.id) já está no @PreAuthorize
        ClienteResponseDTO updatedCliente = clienteService.update(id, requestDTO);
        return ResponseEntity.ok(updatedCliente);
    }
}
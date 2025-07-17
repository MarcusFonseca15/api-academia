package sync.fit.api.controller;

import sync.fit.api.dto.request.ClienteAdminUpdateDTO;
import sync.fit.api.dto.request.ClienteUpdateProfileDTO;
import sync.fit.api.dto.request.ClienteRegisterRequestDTO;
import sync.fit.api.dto.response.ClienteResponseDTO;
import sync.fit.api.model.Cliente;
import sync.fit.api.model.UserIdentifiable;
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
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> getAllClientes() {
        List<ClienteResponseDTO> clientes = clienteService.findAll();
        return ResponseEntity.ok(clientes);
    }

    // ADMIN, INSTRUTOR podem ver qualquer cliente.
    // CLIENTE só pode ver a si mesmo (se o ID na URL for o seu próprio ID logado).
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUTOR') or (hasRole('CLIENTE') and #id == authentication.principal.id)")
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> getClienteById(@PathVariable Long id) {
        ClienteResponseDTO cliente = clienteService.findById(id);
        return ResponseEntity.ok(cliente);
    }

    // Apenas ADMIN pode criar um cliente
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> createCliente(@Valid @RequestBody ClienteRegisterRequestDTO requestDTO) {
        ClienteResponseDTO cliente = clienteService.save(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
    }

    // Apenas ADMIN pode atualizar um cliente específico
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> updateCliente(
            @PathVariable Long id,
            @Valid @RequestBody ClienteAdminUpdateDTO requestDTO) {
        ClienteResponseDTO cliente = clienteService.updateByAdmin(id, requestDTO);
        return ResponseEntity.ok(cliente);
    }

    // Apenas ADMIN pode deletar um cliente
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id) {
        clienteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Cliente pode ver seu próprio perfil
    @PreAuthorize("hasRole('CLIENTE')")
    @GetMapping("/meu-perfil")
    public ResponseEntity<ClienteResponseDTO> getMeuPerfil() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserIdentifiable userLogado = (UserIdentifiable) authentication.getPrincipal();

        if (!(userLogado instanceof Cliente)) {
            throw new SecurityException("Acesso negado: Usuário autenticado não é um Cliente.");
        }
        ClienteResponseDTO meuPerfil = clienteService.findById(userLogado.getId());
        return ResponseEntity.ok(meuPerfil);
    }

    // Cliente pode atualizar seu próprio perfil
    @PreAuthorize("hasRole('CLIENTE')")
    @PutMapping("/meu-perfil")
    public ResponseEntity<ClienteResponseDTO> updateMeuPerfil(@Valid @RequestBody ClienteUpdateProfileDTO requestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserIdentifiable userLogado = (UserIdentifiable) authentication.getPrincipal();

        if (!(userLogado instanceof Cliente)) {
            throw new SecurityException("Acesso negado: Usuário autenticado não é um Cliente.");
        }
        ClienteResponseDTO updatedCliente = clienteService.updateProfile(userLogado.getId(), requestDTO);
        return ResponseEntity.ok(updatedCliente);
    }
}
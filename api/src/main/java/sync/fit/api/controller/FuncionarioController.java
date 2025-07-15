package sync.fit.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

// Importe os DTOs atualizados (sem cargoId)
import sync.fit.api.dto.request.AdministradorRequestDTO; // Agora sem cargoId
import sync.fit.api.dto.request.FuncionarioRequestDTO;
import sync.fit.api.dto.request.InstrutorRequestDTO;     // Agora sem cargoId
import sync.fit.api.dto.response.FuncionarioResponseDTO;
import sync.fit.api.service.FuncionarioService;

import java.util.List;

@RestController
@RequestMapping("/api/funcionarios")
@RequiredArgsConstructor
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    // Criar Administrador: Apenas ADMIN pode fazer isso
    @PreAuthorize("hasRole('ADMIN')") // Use o nome completo da Role
    @PostMapping("/administradores")
    public ResponseEntity<FuncionarioResponseDTO> criarAdministrador(@Valid @RequestBody AdministradorRequestDTO dto) {
        // O cargo é definido internamente no FuncionarioService.criarAdministrador
        FuncionarioResponseDTO novoAdministrador = funcionarioService.criarAdministrador(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoAdministrador);
    }

    // Criar Instrutor: Apenas ADMIN pode fazer isso
    @PreAuthorize("hasRole('ADMIN')") // Use o nome completo da Role
    @PostMapping("/instrutores")
    public ResponseEntity<FuncionarioResponseDTO> criarInstrutor(@Valid @RequestBody InstrutorRequestDTO dto) {
        // O cargo é definido internamente no FuncionarioService.criarInstrutor
        FuncionarioResponseDTO novoInstrutor = funcionarioService.criarInstrutor(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoInstrutor);
    }

    // Listar todos os funcionários: Apenas ADMIN ou Recepcionista
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    @GetMapping
    public ResponseEntity<List<FuncionarioResponseDTO>> listarTodosFuncionarios() {
        List<FuncionarioResponseDTO> funcionarios = funcionarioService.listarTodos();
        return ResponseEntity.ok(funcionarios);
    }

    // Buscar funcionário por ID: ADMIN, INSTRUTOR, RECEPCIONISTA.
    // Instrutor pode ver a si mesmo. Recepcionista também.
    // ADMIN pode ver qualquer um.
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUTOR', 'RECEPCIONISTA') or (hasRole('INSTRUTOR') and #id == authentication.principal.id) or (hasRole('RECEPCIONISTA') and #id == authentication.principal.id)")
    @GetMapping("/{id}")
    public ResponseEntity<FuncionarioResponseDTO> buscarFuncionarioPorId(@PathVariable Long id) {
        FuncionarioResponseDTO funcionario = funcionarioService.buscarPorId(id);
        return ResponseEntity.ok(funcionario);
    }

    // Atualizar funcionário: Apenas ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<FuncionarioResponseDTO> atualizarFuncionario(
            @PathVariable Long id,
            @Valid @RequestBody FuncionarioRequestDTO dto) { // Use o DTO genérico para atualização
        FuncionarioResponseDTO funcionarioAtualizado = funcionarioService.atualizar(id, dto);
        return ResponseEntity.ok(funcionarioAtualizado);
    }

    // Deletar funcionário: Apenas ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarFuncionario(@PathVariable Long id) {
        funcionarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint para um funcionário ver e atualizar seu próprio perfil (opcional, se não for genérico)
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUTOR', 'RECEPCIONISTA')")
    @GetMapping("/meu-perfil")
    public ResponseEntity<FuncionarioResponseDTO> getMeuPerfilFuncionario() {
        // A lógica de segurança já garante que authentication.principal é o funcionário logado
        Long idLogado = ((sync.fit.api.model.UserIdentifiable) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        FuncionarioResponseDTO meuPerfil = funcionarioService.buscarPorId(idLogado);
        return ResponseEntity.ok(meuPerfil);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUTOR', 'RECEPCIONISTA')")
    @PutMapping("/meu-perfil")
    public ResponseEntity<FuncionarioResponseDTO> updateMeuPerfilFuncionario(@Valid @RequestBody FuncionarioRequestDTO dto) {
        Long idLogado = ((sync.fit.api.model.UserIdentifiable) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        FuncionarioResponseDTO updatedFuncionario = funcionarioService.atualizar(idLogado, dto);
        return ResponseEntity.ok(updatedFuncionario);
    }
}
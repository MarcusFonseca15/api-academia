// sync.fit.api.controller.FuncionarioController.java
// Não há mudanças significativas aqui em relação ao que você já tinha,
// apenas a confirmação de que ele continua criando Instrutores.
// Lembre-se de adicionar @PreAuthorize para proteger esses endpoints.
package sync.fit.api.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Importe PreAuthorize
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import sync.fit.api.dto.request.AdministradorRequestDTO;
import sync.fit.api.dto.request.FuncionarioRequestDTO;
import sync.fit.api.dto.request.InstrutorRequestDTO;
import sync.fit.api.dto.response.FuncionarioResponseDTO;
import sync.fit.api.service.FuncionarioService;

import java.util.List;

@RestController
@RequestMapping("/api/funcionarios")
@RequiredArgsConstructor
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    @PreAuthorize("hasRole('ADMIN')") // Exemplo de proteção
    @PostMapping("/administradores")
    public ResponseEntity<FuncionarioResponseDTO> criarAdministrador(@Valid @RequestBody AdministradorRequestDTO dto) {
        FuncionarioResponseDTO novoAdministrador = funcionarioService.criarAdministrador(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoAdministrador);
    }

    @PreAuthorize("hasRole('ADMIN')") // Exemplo de proteção
    @PostMapping("/instrutores")
    public ResponseEntity<FuncionarioResponseDTO> criarInstrutor(@Valid @RequestBody InstrutorRequestDTO dto) {
        FuncionarioResponseDTO novoInstrutor = funcionarioService.criarInstrutor(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoInstrutor);
    }

    // ... Outros endpoints (recepcionistas, listar, buscar, atualizar, deletar)
    // Proteja-os com @PreAuthorize conforme sua regra de negócio.

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<FuncionarioResponseDTO>> listarTodosFuncionarios() {
        List<FuncionarioResponseDTO> funcionarios = funcionarioService.listarTodos();
        return ResponseEntity.ok(funcionarios);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUTOR', 'RECEPCIONISTA')") // Exemplo
    @GetMapping("/{id}")
    public ResponseEntity<FuncionarioResponseDTO> buscarFuncionarioPorId(@PathVariable Long id) {
        FuncionarioResponseDTO funcionario = funcionarioService.buscarPorId(id);
        return ResponseEntity.ok(funcionario);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<FuncionarioResponseDTO> atualizarFuncionario(
            @PathVariable Long id,
            @Valid @RequestBody FuncionarioRequestDTO dto) {
        FuncionarioResponseDTO funcionarioAtualizado = funcionarioService.atualizar(id, dto);
        return ResponseEntity.ok(funcionarioAtualizado);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarFuncionario(@PathVariable Long id) {
        funcionarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
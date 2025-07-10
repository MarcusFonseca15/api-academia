package sync.fit.api.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid; // Importar @Valid
import lombok.RequiredArgsConstructor; // Importar RequiredArgsConstructor (se usar Lombok)

import sync.fit.api.dto.request.AdministradorRequestDTO; // Importar DTO de Administrador
import sync.fit.api.dto.request.FuncionarioRequestDTO;
import sync.fit.api.dto.request.InstrutorRequestDTO;    // Importar DTO de Instrutor
import sync.fit.api.dto.request.RecepcionistaRequestDTO; // Importar DTO de Recepcionista
import sync.fit.api.dto.response.FuncionarioResponseDTO;
import sync.fit.api.service.FuncionarioService;

import java.util.List;

@RestController
@RequestMapping("/api/funcionarios") // Rota base para operações gerais de funcionários
@RequiredArgsConstructor // Usar injeção de dependência via construtor (Lombok)
public class FuncionarioController {

    private final FuncionarioService funcionarioService; // Campo final para injeção via construtor

    // --- Endpoints de Criação Específicos por Tipo ---

    @PostMapping("/administradores") // Exemplo: POST /api/funcionarios/administradores
    public ResponseEntity<FuncionarioResponseDTO> criarAdministrador(@Valid @RequestBody AdministradorRequestDTO dto) {
        FuncionarioResponseDTO novoAdministrador = funcionarioService.criarAdministrador(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoAdministrador);
    }

    @PostMapping("/instrutores") // Exemplo: POST /api/funcionarios/instrutores
    public ResponseEntity<FuncionarioResponseDTO> criarInstrutor(@Valid @RequestBody InstrutorRequestDTO dto) {
        FuncionarioResponseDTO novoInstrutor = funcionarioService.criarInstrutor(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoInstrutor);
    }

//    @PostMapping("/recepcionistas") // Exemplo: POST /api/funcionarios/recepcionistas
//    public ResponseEntity<FuncionarioResponseDTO> criarRecepcionista(@Valid @RequestBody RecepcionistaRequestDTO dto) {
//        FuncionarioResponseDTO novaRecepcionista = funcionarioService.criarRecepcionista(dto);
//        return ResponseEntity.status(HttpStatus.CREATED).body(novaRecepcionista);
//    }

    // --- Endpoints para Operações Genéricas (Listar, Buscar por ID, Atualizar, Deletar) ---

    @GetMapping // GET /api/funcionarios
    public ResponseEntity<List<FuncionarioResponseDTO>> listarTodosFuncionarios() {
        List<FuncionarioResponseDTO> funcionarios = funcionarioService.listarTodos();
        return ResponseEntity.ok(funcionarios);
    }

    @GetMapping("/{id}") // GET /api/funcionarios/{id}
    public ResponseEntity<FuncionarioResponseDTO> buscarFuncionarioPorId(@PathVariable Long id) {
        FuncionarioResponseDTO funcionario = funcionarioService.buscarPorId(id);
        return ResponseEntity.ok(funcionario);
    }

    // O endpoint de atualização pode ser um desafio com herança,
    // pois o DTO genérico pode não ter os campos específicos.
    // Você pode ter:
    // 1. Um PUT genérico que só atualiza campos comuns (como este exemplo abaixo).
    // 2. PUTs específicos por tipo (ex: PUT /api/funcionarios/instrutores/{id}).
    // A complexidade depende da sua necessidade de atualizar campos específicos via API.
    @PutMapping("/{id}") // PUT /api/funcionarios/{id}
    public ResponseEntity<FuncionarioResponseDTO> atualizarFuncionario(
            @PathVariable Long id,
            @Valid @RequestBody FuncionarioRequestDTO dto) { // Usa o DTO genérico para campos comuns
        FuncionarioResponseDTO funcionarioAtualizado = funcionarioService.atualizar(id, dto);
        return ResponseEntity.ok(funcionarioAtualizado);
    }

    @DeleteMapping("/{id}") // DELETE /api/funcionarios/{id}
    public ResponseEntity<Void> deletarFuncionario(@PathVariable Long id) {
        funcionarioService.deletar(id);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content
    }
}
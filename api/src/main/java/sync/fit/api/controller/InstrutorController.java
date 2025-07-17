package sync.fit.api.controller;

import sync.fit.api.dto.response.ClienteResponseDTO;
import sync.fit.api.dto.response.InstrutorResponseDTO;
import sync.fit.api.model.Funcionario;
import sync.fit.api.service.InstrutorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/instrutores")
@RequiredArgsConstructor
public class InstrutorController {

    private final InstrutorService instrutorService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<InstrutorResponseDTO>> getAllInstrutores() {
        List<InstrutorResponseDTO> instrutores = instrutorService.findAll();
        return ResponseEntity.ok(instrutores);
    }

    @PreAuthorize("hasRole('ADMIN') or (hasRole('INSTRUTOR') and #id == authentication.principal.id)")
    @GetMapping("/{id}")
    public ResponseEntity<InstrutorResponseDTO> getInstrutorById(@PathVariable Long id) {
        InstrutorResponseDTO instrutor = instrutorService.findById(id);
        return ResponseEntity.ok(instrutor);
    }

    @PreAuthorize("hasRole('INSTRUTOR')")
    @GetMapping("/meus-alunos")
    public ResponseEntity<List<ClienteResponseDTO>> getMeusAlunos() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Funcionario instrutorLogado = (Funcionario) authentication.getPrincipal();

        List<ClienteResponseDTO> meusAlunos = instrutorService.findAlunosByInstrutorId(instrutorLogado.getId());
        return ResponseEntity.ok(meusAlunos);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/alunos")
    public ResponseEntity<List<ClienteResponseDTO>> getAlunosDoInstrutor(@PathVariable Long id) {
        List<ClienteResponseDTO> alunos = instrutorService.findAlunosByInstrutorId(id);
        return ResponseEntity.ok(alunos);
    }
}
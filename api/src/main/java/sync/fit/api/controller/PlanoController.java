package sync.fit.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sync.fit.api.dto.request.PlanoRequestDTO;
import sync.fit.api.dto.response.PlanoResponseDTO;
import sync.fit.api.service.PlanoService;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/planos")
public class PlanoController {

    @Autowired
    private PlanoService planoService;

    // Criar Plano: Apenas ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public PlanoResponseDTO criar(@Valid @RequestBody PlanoRequestDTO dto) {
        return planoService.criar(dto);
    }


    @GetMapping
    public List<PlanoResponseDTO> listarTodos() {
        return planoService.listarTodos();
    }


    @GetMapping("/{id}")
    public ResponseEntity<PlanoResponseDTO> buscarPorId(@PathVariable Long id) {
        PlanoResponseDTO plano = planoService.buscarPorId(id);
        return ResponseEntity.ok(plano);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PlanoResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PlanoRequestDTO dto) { // Adicionado @Valid
        PlanoResponseDTO planoAtualizado = planoService.atualizar(id, dto);
        return ResponseEntity.ok(planoAtualizado);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        planoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
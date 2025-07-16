package sync.fit.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sync.fit.api.dto.request.TreinoRequestDTO;
import sync.fit.api.dto.response.TreinoResponseDTO;
import sync.fit.api.service.TreinoService;
import java.util.List;

@RestController
@RequestMapping("/api/treinos")
public class TreinoController {

    @Autowired
    private TreinoService treinoService;

    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUTOR')")
    @PostMapping
    public TreinoResponseDTO criar(@RequestBody TreinoRequestDTO dto) {
        return treinoService.criar(dto);
    }


    @GetMapping
    public List<TreinoResponseDTO> listarTodos() {
        return treinoService.listarTodos();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUTOR')")
    @PutMapping("/{id}")
    public ResponseEntity<TreinoResponseDTO> atualizar(@PathVariable Long id, @RequestBody TreinoRequestDTO dto) {
        return ResponseEntity.ok(treinoService.atualizarTreino(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUTOR')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        treinoService.deletarTreino(id);
        return ResponseEntity.noContent().build();
    }
}

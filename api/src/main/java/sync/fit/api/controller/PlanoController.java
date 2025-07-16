package sync.fit.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sync.fit.api.dto.request.PlanoRequestDTO;
import sync.fit.api.dto.response.PlanoResponseDTO;
import sync.fit.api.service.PlanoService;

import java.util.List;

@RestController
@RequestMapping("/api/planos")
public class PlanoController {

    @Autowired
    private PlanoService planoService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public PlanoResponseDTO criar(@RequestBody PlanoRequestDTO dto) {
        return planoService.criar(dto);
    }

    @GetMapping
    public List<PlanoResponseDTO> listarTodos() {
        return planoService.listarTodos();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        planoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

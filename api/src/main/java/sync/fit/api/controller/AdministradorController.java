package sync.fit.api.controller;

import sync.fit.api.dto.response.AdministradorResponseDTO;
import sync.fit.api.service.AdministradorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/administradores")
@RequiredArgsConstructor
public class AdministradorController {

    private final AdministradorService administradorService;

    // Apenas ADMIN pode listar todos os administradores
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<AdministradorResponseDTO>> getAllAdministradores() {
        List<AdministradorResponseDTO> administradores = administradorService.findAllAdministradores();
        return ResponseEntity.ok(administradores);
    }

    // Apenas ADMIN pode buscar um administrador específico
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<AdministradorResponseDTO> getAdministradorById(@PathVariable Long id) {
        AdministradorResponseDTO administrador = administradorService.findAdministradorById(id);
        return ResponseEntity.ok(administrador);
    }

    // Opcional: Endpoints para criar, atualizar e deletar Administradores, se não forem feitos no FuncionarioController
    // @PostMapping
    // @PreAuthorize("hasRole('ADMIN')")
    // public ResponseEntity<AdministradorResponseDTO> createAdministrador(@Valid @RequestBody AdministradorRequestDTO dto) { ... }

    // @PutMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    // public ResponseEntity<AdministradorResponseDTO> updateAdministrador(@PathVariable Long id, @Valid @RequestBody AdministradorRequestDTO dto) { ... }

    // @DeleteMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    // public ResponseEntity<Void> deleteAdministrador(@PathVariable Long id) { ... }
}
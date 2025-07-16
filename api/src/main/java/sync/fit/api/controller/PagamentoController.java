package sync.fit.api.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import sync.fit.api.dto.request.PagamentoRequestDTO;
import sync.fit.api.dto.response.PagamentoResponseDTO;
import sync.fit.api.service.PagamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagamentos")
@RequiredArgsConstructor
public class PagamentoController {

    private final PagamentoService pagamentoService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<PagamentoResponseDTO>> getAll() {
        return ResponseEntity.ok(pagamentoService.findAll());
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<PagamentoResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(pagamentoService.findById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<PagamentoResponseDTO>> getByClienteId(@PathVariable Long clienteId) {
        List<PagamentoResponseDTO> pagamentos = pagamentoService.findByClienteId(clienteId);
        return ResponseEntity.ok(pagamentos);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping
    public ResponseEntity<PagamentoResponseDTO> create(@Valid @RequestBody PagamentoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pagamentoService.save(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PagamentoResponseDTO> update(@PathVariable Long id,
            @Valid @RequestBody PagamentoRequestDTO dto) {
        return ResponseEntity.ok(pagamentoService.update(id, dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        pagamentoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

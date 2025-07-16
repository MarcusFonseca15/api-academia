package sync.fit.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sync.fit.api.dto.request.AvaliacaoFisicaRequestDTO;
import sync.fit.api.dto.response.AvaliacaoFisicaResponseDTO;
import sync.fit.api.model.Cliente;
import sync.fit.api.model.UserIdentifiable;
import sync.fit.api.service.AvaliacaoFisicaService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


import java.util.List;

@RestController
@RequestMapping("/api/avaliacoes")
@RequiredArgsConstructor
public class AvaliacaoFisicaController {

    private final AvaliacaoFisicaService avaliacaoService;


    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUTOR')")
    @PostMapping
    public ResponseEntity<AvaliacaoFisicaResponseDTO> criar(@Valid @RequestBody AvaliacaoFisicaRequestDTO dto) {
        AvaliacaoFisicaResponseDTO novaAvaliacao = avaliacaoService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaAvaliacao);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUTOR') or (hasRole('CLIENTE') and #clienteId == authentication.principal.id)")
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<AvaliacaoFisicaResponseDTO>> listarPorCliente(@PathVariable Long clienteId) {
        List<AvaliacaoFisicaResponseDTO> avaliacoes = avaliacaoService.listarPorCliente(clienteId);
        return ResponseEntity.ok(avaliacoes);
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUTOR') or (hasRole('CLIENTE') and @avaliacaoFisicaService.buscarPorId(#id).clienteId == authentication.principal.id)")
    @GetMapping("/{id}")
    public ResponseEntity<AvaliacaoFisicaResponseDTO> buscarPorId(@PathVariable Long id) {
        AvaliacaoFisicaResponseDTO avaliacao = avaliacaoService.buscarPorId(id);
        return ResponseEntity.ok(avaliacao);
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUTOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        avaliacaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
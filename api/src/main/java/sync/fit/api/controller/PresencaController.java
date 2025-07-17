package sync.fit.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import sync.fit.api.dto.response.PresencaResponse;
import sync.fit.api.model.UserIdentifiable;
import sync.fit.api.service.PresencaService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/presencas")
public class PresencaController {

    @Autowired
    private PresencaService presencaService;

    // Rota para o cliente marcar presença para si mesmo
    @PostMapping("/marcar")
    @PreAuthorize("hasRole('CLIENTE')") // Apenas CLIENTE pode marcar presença para si mesmo
    public ResponseEntity<PresencaResponse> marcarMinhaPresenca() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserIdentifiable userLogado = (UserIdentifiable) authentication.getPrincipal();

        // Verifica se o usuário logado é realmente um Cliente
        if (!(userLogado instanceof sync.fit.api.model.Cliente)) {
            throw new IllegalArgumentException("Somente clientes podem marcar presença nesta rota.");
        }

        Long clienteId = userLogado.getId();

        PresencaResponse presenca = presencaService.registrarPresenca(clienteId);
        return ResponseEntity.ok(presenca);
    }

    // Rota para administradores/instrutores marcarem presença para um cliente específico
    @PostMapping("/cliente/{clienteId}/marcar")
    @PreAuthorize("hasAnyRole('INSTRUTOR', 'ADMIN')")
    public ResponseEntity<PresencaResponse> marcarPresencaParaCliente(@PathVariable Long clienteId) {
        PresencaResponse presenca = presencaService.registrarPresenca(clienteId);
        return ResponseEntity.ok(presenca);
    }

    // Rota para o cliente ver seu próprio histórico de presença
    @GetMapping("/historico")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<PresencaResponse>> getMeuHistoricoCliente() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserIdentifiable userLogado = (UserIdentifiable) authentication.getPrincipal();

        if (!(userLogado instanceof sync.fit.api.model.Cliente)) {
            throw new IllegalArgumentException("Somente clientes podem acessar seu histórico nesta rota.");
        }

        Long clienteId = userLogado.getId();

        List<PresencaResponse> historico = presencaService.getHistoricoPresencaCliente(clienteId);
        return ResponseEntity.ok(historico);
    }

    // Rota para administradores/instrutores verem o histórico de um cliente específico
    @GetMapping("/cliente/{clienteId}/historico")
    @PreAuthorize("hasAnyRole('INSTRUTOR', 'ADMIN')")
    public ResponseEntity<List<PresencaResponse>> getHistoricoClienteEspecifico(@PathVariable Long clienteId) {
        List<PresencaResponse> historico = presencaService.getHistoricoPresencaCliente(clienteId);
        return ResponseEntity.ok(historico);
    }

    // Rota para administradores/instrutores verem a frequência geral
    @GetMapping("/geral")
    @PreAuthorize("hasAnyRole('INSTRUTOR', 'ADMIN')")
    public ResponseEntity<List<PresencaResponse>> getFrequenciaGeral(
            @RequestParam("dataInicio") String dataInicio,
            @RequestParam("dataFim") String dataFim) {
        // Parsear as datas. Usar um formato ISO_LOCAL_DATE_TIME (YYYY-MM-DDTHH:MM:SS)
        LocalDateTime start = LocalDateTime.parse(dataInicio);
        LocalDateTime end = LocalDateTime.parse(dataFim);
        List<PresencaResponse> frequencia = presencaService.getFrequenciaGeral(start, end);
        return ResponseEntity.ok(frequencia);
    }
}
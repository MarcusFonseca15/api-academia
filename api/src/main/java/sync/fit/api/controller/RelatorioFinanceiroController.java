package sync.fit.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sync.fit.api.dto.response.RelatorioClientesPorPlanoDTO;
import sync.fit.api.service.RelatorioFinanceiroService;

import java.util.List;

import sync.fit.api.dto.response.RelatorioSalarioInstrutorDTO;
import io.swagger.v3.oas.annotations.Operation;

import sync.fit.api.dto.response.RelatorioFaturamentoDTO;

@RestController
@RequestMapping("/relatorios")
public class RelatorioFinanceiroController {

    @Autowired
    private RelatorioFinanceiroService relatorioFinanceiroService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/clientes-por-plano")
    public ResponseEntity<List<RelatorioClientesPorPlanoDTO>> listarClientesPorPlano() {
        List<RelatorioClientesPorPlanoDTO> relatorio = relatorioFinanceiroService.obterRelatorioClientesPorPlano();
        return ResponseEntity.ok(relatorio);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/salario-instrutores")
    public ResponseEntity<List<RelatorioSalarioInstrutorDTO>> listarSalarioInstrutores() {
        List<RelatorioSalarioInstrutorDTO> salarios = relatorioFinanceiroService.obterRelatorioSalarioInstrutores();
        return ResponseEntity.ok(salarios);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/faturamento")
    public ResponseEntity<RelatorioFaturamentoDTO> obterRelatorioFaturamento() {
        RelatorioFaturamentoDTO dto = relatorioFinanceiroService.gerarRelatorioFaturamento();
        return ResponseEntity.ok(dto);
    }
}
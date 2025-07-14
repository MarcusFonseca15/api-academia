package sync.fit.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sync.fit.api.dto.response.RelatorioClientesPorPlanoDTO;
import sync.fit.api.service.RelatorioFinanceiroService;

import java.util.List;

@RestController
@RequestMapping("/relatorios")
public class RelatorioFinanceiroController {

    @Autowired
    private RelatorioFinanceiroService relatorioFinanceiroService;

    @GetMapping("/clientes-por-plano")
    public ResponseEntity<List<RelatorioClientesPorPlanoDTO>> listarClientesPorPlano() {
        List<RelatorioClientesPorPlanoDTO> relatorio = relatorioFinanceiroService.obterRelatorioClientesPorPlano();
        return ResponseEntity.ok(relatorio);
    }
}
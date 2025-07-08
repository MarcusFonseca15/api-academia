package sync.fit.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sync.fit.api.dto.request.AvaliacaoFisicaRequestDTO;
import sync.fit.api.dto.response.AvaliacaoFisicaResponseDTO;
import sync.fit.api.service.AvaliacaoFisicaService;

import java.util.List;

@RestController
@RequestMapping("/avaliacoes")
public class AvaliacaoFisicaController {

    @Autowired
    private AvaliacaoFisicaService avaliacaoService;

    @PostMapping
    public AvaliacaoFisicaResponseDTO criar(@RequestBody AvaliacaoFisicaRequestDTO dto) {
        return avaliacaoService.criar(dto);
    }

    @GetMapping("/cliente/{id}")
    public List<AvaliacaoFisicaResponseDTO> listarPorCliente(@PathVariable Long id) {
        return avaliacaoService.listarPorCliente(id);
    }
}

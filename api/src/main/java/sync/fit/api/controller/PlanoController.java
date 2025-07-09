package sync.fit.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping
    public PlanoResponseDTO criar(@RequestBody PlanoRequestDTO dto) {
        return planoService.criar(dto);
    }

    @GetMapping
    public List<PlanoResponseDTO> listarTodos() {
        return planoService.listarTodos();
    }
}

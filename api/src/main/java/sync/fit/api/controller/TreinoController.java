package sync.fit.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sync.fit.api.dto.request.TreinoRequestDTO;
import sync.fit.api.dto.response.TreinoResponseDTO;
import sync.fit.api.service.TreinoService;

import java.util.List;

@RestController
@RequestMapping("/treinos")
public class TreinoController {

    @Autowired
    private TreinoService treinoService;

    @PostMapping
    public TreinoResponseDTO criar(@RequestBody TreinoRequestDTO dto) {
        return treinoService.criar(dto);
    }

    @GetMapping
    public List<TreinoResponseDTO> listarTodos() {
        return treinoService.listarTodos();
    }
}

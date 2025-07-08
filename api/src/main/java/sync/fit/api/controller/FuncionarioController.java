package sync.fit.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sync.fit.api.dto.request.FuncionarioRequestDTO;
import sync.fit.api.dto.response.FuncionarioResponseDTO;
import sync.fit.api.service.FuncionarioService;

import java.util.List;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {

    @Autowired
    private FuncionarioService funcionarioService;

    @PostMapping
    public FuncionarioResponseDTO criar(@RequestBody FuncionarioRequestDTO dto) {
        return funcionarioService.criar(dto);
    }

    @GetMapping
    public List<FuncionarioResponseDTO> listarTodos() {
        return funcionarioService.listarTodos();
    }
}

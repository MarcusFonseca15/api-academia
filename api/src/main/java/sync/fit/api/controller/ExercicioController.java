package sync.fit.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sync.fit.api.dto.request.ExercicioRequestDTO;
import sync.fit.api.dto.response.ExercicioResponseDTO;
import sync.fit.api.service.ExercicioService;

import java.util.List;

@RestController
@RequestMapping("/api/exercicios")
public class ExercicioController {

    @Autowired
    private ExercicioService exercicioService;

//    @PostMapping
//    public ExercicioResponseDTO criar(@RequestBody ExercicioRequestDTO dto) {
//        return exercicioService.criar(dto);
//    }
//
//    @GetMapping
//    public List<ExercicioResponseDTO> listarTodos() {
//        return exercicioService.listarTodos();
//    }
}

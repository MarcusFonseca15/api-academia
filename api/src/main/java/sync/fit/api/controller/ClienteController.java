//package sync.fit.api.controller;
//
////import sync.fit.api.dto.ClienteRequestDTO;
//import sync.fit.api.dto.ClienteResponseDTO;
//import sync.fit.api.service.ClienteService;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/clientes")
//public class ClienteController {
//
//    @Autowired
//    private ClienteService clienteService;
//
//    @PostMapping
//    public ClienteResponseDTO criar(@RequestBody @Valid ClienteRequestDTO dto) {
//        return clienteService.criar(dto);
//    }
//
//    @GetMapping
//    public List<ClienteResponseDTO> listarTodos() {
//        return clienteService.listarTodos();
//    }
//}

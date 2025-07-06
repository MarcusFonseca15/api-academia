package sync.fit.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/clientes")
public class ApiController {

    private List<String> clientes = new ArrayList<>();

    private ObjectMapper objectMapper;

    public ApiController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public ResponseEntity<String> listClientes() throws JsonProcessingException {
        return ResponseEntity.ok(objectMapper.writeValueAsString(clientes));
    }

    @PostMapping
    public ResponseEntity<String> addClient(@RequestBody String cliente) {
        clientes.add(cliente);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> clearClients() {
        clientes = new ArrayList<>();
        return ResponseEntity.ok().build();
    }
}

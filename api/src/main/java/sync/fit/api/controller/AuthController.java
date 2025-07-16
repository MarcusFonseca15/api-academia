package sync.fit.api.controller;

import sync.fit.api.dto.request.LoginRequestDTO;
import sync.fit.api.dto.request.ClienteRegisterRequestDTO;
import sync.fit.api.dto.request.AdministradorRegisterRequestDTO;
import sync.fit.api.dto.request.InstrutorRegisterRequestDTO;
import sync.fit.api.dto.response.AuthResponseDTO;
import sync.fit.api.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        AuthResponseDTO response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register/cliente")
    public ResponseEntity<AuthResponseDTO> registerCliente(@Valid @RequestBody ClienteRegisterRequestDTO request) {
        AuthResponseDTO response = authService.registerCliente(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Apenas ADMINISTRADOR pode registrar outros funcionários
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register/administrador")
    public ResponseEntity<AuthResponseDTO> registerAdministrador(@Valid @RequestBody AdministradorRegisterRequestDTO request) {
        AuthResponseDTO response = authService.registerAdministrador(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register/instrutor")
    public ResponseEntity<AuthResponseDTO> registerInstrutor(@Valid @RequestBody InstrutorRegisterRequestDTO request) {
        AuthResponseDTO response = authService.registerInstrutor(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
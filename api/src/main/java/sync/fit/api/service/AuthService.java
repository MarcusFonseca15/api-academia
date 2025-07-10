package sync.fit.api.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sync.fit.api.dto.request.LoginRequestDTO;
import sync.fit.api.dto.request.ClienteRegisterRequestDTO; // Ajustar para o DTO de registro
import sync.fit.api.dto.request.AdministradorRegisterRequestDTO; // Novo DTO
import sync.fit.api.dto.request.InstrutorRegisterRequestDTO;     // Novo DTO
import sync.fit.api.dto.response.AuthResponseDTO;
import sync.fit.api.exception.ResourceNotFoundException;

import sync.fit.api.model.Cliente;
import sync.fit.api.model.Funcionario;
import sync.fit.api.model.Administrador; // Importar
import sync.fit.api.model.Instrutor;     // Importar

import sync.fit.api.model.Cargo;         // Importar
import sync.fit.api.model.Plano;         // Importar

import sync.fit.api.repository.ClienteRepository;
import sync.fit.api.repository.FuncionarioRepository;
import sync.fit.api.repository.CargoRepository;   // Importar
import sync.fit.api.repository.PlanoRepository;   // Importar

import sync.fit.api.security.JwtService;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final ClienteRepository clienteRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final CargoRepository cargoRepository; // Injetar
    private final PlanoRepository planoRepository; // Injetar

    public AuthResponseDTO login(LoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwt = jwtService.generateToken(userDetails);

        String tipo = userDetails.getAuthorities().stream()
                .filter(a -> a.getAuthority().startsWith("ROLE_"))
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .findFirst()
                .orElse("UNKNOWN");

        return new AuthResponseDTO(jwt, tipo, userDetails.getUsername());
    }

    @Transactional
    public AuthResponseDTO registerCliente(ClienteRegisterRequestDTO dto) {
        if (clienteRepository.findByEmail(dto.getEmail()).isPresent() ||
                funcionarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado.");
        }

        Plano plano = planoRepository.findById(dto.getPlanoId())
                .orElseThrow(() -> new ResourceNotFoundException("Plano não encontrado com ID: " + dto.getPlanoId()));

        // Para registrar cliente, o administrador que está registrando já deve existir e ser do tipo Administrador
        // Se este registro for público (cliente se auto-registrando), você pode remover a dependência de administrador.
        Administrador administrador = (Administrador) funcionarioRepository.findById(dto.getAdministradorId())
                .filter(f -> f instanceof Administrador) // Garante que é um Administrador
                .orElseThrow(() -> new ResourceNotFoundException("Administrador não encontrado ou não é um ADMIN com ID: " + dto.getAdministradorId()));

        Cliente cliente = new Cliente(
                dto.getNome(),
                dto.getEmail(),
                passwordEncoder.encode(dto.getSenha()), // Criptografa!
                dto.getTelefone(),
                plano,
                administrador
        );
        clienteRepository.save(cliente);

        UserDetails userDetails = clienteRepository.findByEmail(cliente.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Erro ao carregar cliente recém-registrado para token."));

        String jwt = jwtService.generateToken(userDetails);
        return new AuthResponseDTO(jwt, "CLIENTE", userDetails.getUsername());
    }

    @Transactional
    public AuthResponseDTO registerAdministrador(AdministradorRegisterRequestDTO dto) {
        if (funcionarioRepository.findByEmail(dto.getEmail()).isPresent() ||
                clienteRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado.");
        }
        Cargo cargo = cargoRepository.findById(dto.getCargoId())
                .orElseThrow(() -> new ResourceNotFoundException("Cargo não encontrado com ID: " + dto.getCargoId()));

        Administrador administrador = new Administrador(
                dto.getNome(),
                dto.getEmail(),
                passwordEncoder.encode(dto.getSenha()), // Criptografa!
                cargo,
                dto.getSalario()
        );
        funcionarioRepository.save(administrador);

        UserDetails userDetails = funcionarioRepository.findByEmail(administrador.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Erro ao carregar administrador recém-registrado para token."));

        String jwt = jwtService.generateToken(userDetails);
        return new AuthResponseDTO(jwt, "ADMINISTRADOR", userDetails.getUsername());
    }

    @Transactional
    public AuthResponseDTO registerInstrutor(InstrutorRegisterRequestDTO dto) {
        if (funcionarioRepository.findByEmail(dto.getEmail()).isPresent() ||
                clienteRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado.");
        }
        Cargo cargo = cargoRepository.findById(dto.getCargoId())
                .orElseThrow(() -> new ResourceNotFoundException("Cargo não encontrado com ID: " + dto.getCargoId()));

        Instrutor instrutor = new Instrutor(
                dto.getNome(),
                dto.getEmail(),
                passwordEncoder.encode(dto.getSenha()), // Criptografa!
                cargo,
                dto.getSalario(),
                dto.getEspecialidade()
        );
        funcionarioRepository.save(instrutor);

        UserDetails userDetails = funcionarioRepository.findByEmail(instrutor.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Erro ao carregar instrutor recém-registrado para token."));

        String jwt = jwtService.generateToken(userDetails);
        return new AuthResponseDTO(jwt, "INSTRUTOR", userDetails.getUsername());
    }

//    @Transactional
//    public AuthResponseDTO registerRecepcionista(RecepcionistaRegisterRequestDTO dto) {
//        if (funcionarioRepository.findByEmail(dto.getEmail()).isPresent() ||
//                clienteRepository.findByEmail(dto.getEmail()).isPresent()) {
//            throw new IllegalArgumentException("Email já cadastrado.");
//        }
//        Cargo cargo = cargoRepository.findById(dto.getCargoId())
//                .orElseThrow(() -> new ResourceNotFoundException("Cargo não encontrado com ID: " + dto.getCargoId()));
//
//        Recepcionista recepcionista = new Recepcionista(
//                dto.getNome(),
//                dto.getEmail(),
//                passwordEncoder.encode(dto.getSenha()), // Criptografa!
//                cargo,
//                dto.getSalario(),
//                dto.getTurnoPreferencial()
//        );
//        funcionarioRepository.save(recepcionista);
//
//        UserDetails userDetails = funcionarioRepository.findByEmail(recepcionista.getEmail())
//                .orElseThrow(() -> new ResourceNotFoundException("Erro ao carregar recepcionista recém-registrado para token."));
//
//        String jwt = jwtService.generateToken(userDetails);
//        return new AuthResponseDTO(jwt, "RECEPCIONISTA", userDetails.getUsername());
//    }
}
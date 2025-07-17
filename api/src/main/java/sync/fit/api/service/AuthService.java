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
import sync.fit.api.dto.request.ClienteRegisterRequestDTO;
import sync.fit.api.dto.request.AdministradorRegisterRequestDTO;
import sync.fit.api.dto.request.InstrutorRegisterRequestDTO;
import sync.fit.api.dto.response.AuthResponseDTO;
import sync.fit.api.exception.ResourceNotFoundException;

import sync.fit.api.model.Cliente;
import sync.fit.api.model.Funcionario;
import sync.fit.api.model.Administrador;
import sync.fit.api.model.Instrutor;

import sync.fit.api.model.Cargo;
import sync.fit.api.model.Plano;
import sync.fit.api.model.Role;

import sync.fit.api.repository.ClienteRepository;
import sync.fit.api.repository.FuncionarioRepository;
import sync.fit.api.repository.CargoRepository;
import sync.fit.api.repository.PlanoRepository;
import sync.fit.api.repository.RoleRepository;
import sync.fit.api.repository.InstrutorRepository;

import sync.fit.api.security.JwtService;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final ClienteRepository clienteRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final CargoRepository cargoRepository;
    private final PlanoRepository planoRepository;
    private final RoleRepository roleRepository;
    private final InstrutorRepository instrutorRepository;

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

        Plano plano = null;
        if (dto.getPlanoId() != null) {
            plano = planoRepository.findById(dto.getPlanoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Plano não encontrado com ID: " + dto.getPlanoId()));
        }

        Instrutor instrutor = null;
        if (dto.getInstrutorId() != null) {
            instrutor = instrutorRepository.findById(dto.getInstrutorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Instrutor não encontrado com ID: " + dto.getInstrutorId()));
        }

        Cliente cliente = new Cliente(
                dto.getNome(),
                dto.getEmail(),
                passwordEncoder.encode(dto.getSenha()),
                dto.getDataNascimento(),
                dto.getTelefone(),
                plano,
                instrutor
        );

        Role clienteRole = roleRepository.findByName("ROLE_CLIENTE")
                .orElseThrow(() -> new ResourceNotFoundException("Role ROLE_CLIENTE não encontrada. Verifique seu data.sql ou inicialização de roles."));
        cliente.getRoles().add(clienteRole);

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

        Cargo cargoAdministrador = cargoRepository.findByNomeCargo("Administrador")
                .orElseThrow(() -> new ResourceNotFoundException("Cargo 'Administrador' não encontrado. Verifique seu data.sql ou inicialização de cargos."));

        Administrador administrador = new Administrador(
                dto.getNome(),
                dto.getEmail(),
                passwordEncoder.encode(dto.getSenha()),
                dto.getTelefone(),
                dto.getSalario(),
                cargoAdministrador
        );


        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new ResourceNotFoundException("Role ROLE_ADMIN não encontrada. Verifique seu data.sql ou inicialização de roles."));
        administrador.getRoles().add(adminRole);

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

        Cargo cargoInstrutor = cargoRepository.findByNomeCargo("Instrutor")
                .orElseThrow(() -> new ResourceNotFoundException("Cargo 'Instrutor' não encontrado. Verifique seu data.sql ou inicialização de cargos."));

        Instrutor instrutor = new Instrutor(
                dto.getNome(),
                dto.getEmail(),
                passwordEncoder.encode(dto.getSenha()),
                dto.getTelefone(),
                dto.getSalario(),
                cargoInstrutor,
                dto.getEspecialidade()
        );

        Role instrutorRole = roleRepository.findByName("ROLE_INSTRUTOR")
                .orElseThrow(() -> new ResourceNotFoundException("Role ROLE_INSTRUTOR não encontrada. Verifique seu data.sql ou inicialização de roles."));
        instrutor.getRoles().add(instrutorRole);

        funcionarioRepository.save(instrutor);

        UserDetails userDetails = funcionarioRepository.findByEmail(instrutor.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Erro ao carregar instrutor recém-registrado para token."));

        String jwt = jwtService.generateToken(userDetails);
        return new AuthResponseDTO(jwt, "INSTRUTOR", userDetails.getUsername());
    }
}
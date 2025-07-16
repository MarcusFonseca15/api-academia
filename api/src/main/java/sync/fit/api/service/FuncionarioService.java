package sync.fit.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sync.fit.api.dto.request.FuncionarioRequestDTO;
import sync.fit.api.dto.request.InstrutorRequestDTO;
import sync.fit.api.dto.request.AdministradorRequestDTO;
import sync.fit.api.dto.response.FuncionarioResponseDTO;
import sync.fit.api.model.Funcionario;
import sync.fit.api.model.Cargo;
import sync.fit.api.model.Instrutor;
import sync.fit.api.model.Administrador;
import sync.fit.api.model.Role;
import sync.fit.api.repository.CargoRepository;
import sync.fit.api.repository.FuncionarioRepository;
import sync.fit.api.repository.RoleRepository;
import sync.fit.api.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final CargoRepository cargoRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public FuncionarioResponseDTO criarInstrutor(InstrutorRequestDTO dto) {
        // Busca o Cargo "Instrutor" pelo nome
        Cargo cargoInstrutor = cargoRepository.findByNomeCargo("Instrutor")
                .orElseThrow(() -> new ResourceNotFoundException("Cargo 'Instrutor' não encontrado. Verifique seu data.sql ou inicialização de cargos."));

        Instrutor instrutor = new Instrutor();
        instrutor.setNome(dto.getNome());
        instrutor.setEmail(dto.getEmail());
        instrutor.setSenha(passwordEncoder.encode(dto.getSenha())); // Senha obrigatória para novo registro
        instrutor.setTelefone(dto.getTelefone());
        instrutor.setSalario(dto.getSalario());
        instrutor.setEspecialidade(dto.getEspecialidade());
        instrutor.setCargo(cargoInstrutor); // Atribui o cargo automaticamente

        Role instrutorRole = roleRepository.findByName("ROLE_INSTRUTOR")
                .orElseThrow(() -> new ResourceNotFoundException("Role ROLE_INSTRUTOR não encontrada."));
        instrutor.getRoles().add(instrutorRole);

        Instrutor salvo = funcionarioRepository.save(instrutor);
        return toResponseDTO(salvo);
    }

    @Transactional
    public FuncionarioResponseDTO criarAdministrador(AdministradorRequestDTO dto) {
        // Busca o Cargo "Administrador" pelo nome
        Cargo cargoAdministrador = cargoRepository.findByNomeCargo("Administrador")
                .orElseThrow(() -> new ResourceNotFoundException("Cargo 'Administrador' não encontrado. Verifique seu data.sql ou inicialização de cargos."));

        Administrador administrador = new Administrador();
        administrador.setNome(dto.getNome());
        administrador.setEmail(dto.getEmail());
        administrador.setSenha(passwordEncoder.encode(dto.getSenha())); // Senha obrigatória para novo registro
        administrador.setTelefone(dto.getTelefone());
        administrador.setSalario(dto.getSalario());
        administrador.setCargo(cargoAdministrador); // Atribui o cargo automaticamente


        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new ResourceNotFoundException("Role ROLE_ADMIN não encontrada."));
        administrador.getRoles().add(adminRole);

        Administrador salvo = funcionarioRepository.save(administrador);
        return toResponseDTO(salvo);
    }

    @Transactional(readOnly = true)
    public List<FuncionarioResponseDTO> listarTodos() {
        return funcionarioRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FuncionarioResponseDTO buscarPorId(Long id) {
        Funcionario funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado com ID: " + id));
        return toResponseDTO(funcionario);
    }

    @Transactional
    public FuncionarioResponseDTO atualizar(Long id, FuncionarioRequestDTO dto) {
        Funcionario funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado com ID: " + id));

        // Validação para evitar alteração de email para um já existente
        if (!funcionario.getEmail().equals(dto.getEmail()) && funcionarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado para outro funcionário.");
        }

        funcionario.setNome(dto.getNome());
        funcionario.setEmail(dto.getEmail());
        funcionario.setTelefone(dto.getTelefone());
        funcionario.setSalario(dto.getSalario());



        // Atualização da senha (se fornecida e não vazia)
        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            funcionario.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        Funcionario atualizado = funcionarioRepository.save(funcionario);
        return toResponseDTO(atualizado);
    }

    @Transactional
    public void deletar(Long id) {
        if (!funcionarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Funcionário não encontrado com ID: " + id);
        }
        funcionarioRepository.deleteById(id);
    }

    private FuncionarioResponseDTO toResponseDTO(Funcionario f) {
        FuncionarioResponseDTO dto = new FuncionarioResponseDTO();
        dto.setId(f.getId());
        dto.setNome(f.getNome());
        dto.setEmail(f.getEmail());
        dto.setTelefone(f.getTelefone());
        dto.setSalario(f.getSalario());


        if (f.getCargo() != null) {
            dto.setCargoNome(f.getCargo().getNomeCargo());
            dto.setCargoId(f.getCargo().getId());
        }


        // Adiciona as roles
        Set<String> rolesNames = f.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
        dto.setRoles(rolesNames);

        if (f instanceof Instrutor instrutor) {
            dto.setTipoFuncionario("Instrutor");
            dto.setEspecialidade(instrutor.getEspecialidade());
        } else if (f instanceof Administrador administrador) {
            dto.setTipoFuncionario("Administrador");

        } else {
            dto.setTipoFuncionario("Funcionario Geral");
        }

        return dto;
    }
}
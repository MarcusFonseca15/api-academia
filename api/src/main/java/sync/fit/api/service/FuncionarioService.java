package sync.fit.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private CargoRepository cargoRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public FuncionarioResponseDTO criarInstrutor(InstrutorRequestDTO dto) {
        Cargo cargo = cargoRepository.findById(dto.getCargoId())
                .orElseThrow(() -> new ResourceNotFoundException("Cargo não encontrado com ID: " + dto.getCargoId()));

        Instrutor instrutor = new Instrutor();
        instrutor.setNome(dto.getNome());
        instrutor.setEmail(dto.getEmail());
        instrutor.setSenha(passwordEncoder.encode(dto.getSenha()));
        instrutor.setCargo(cargo);
        instrutor.setSalario(dto.getSalario());
        instrutor.setEspecialidade(dto.getEspecialidade());

        Role instrutorRole = roleRepository.findByName("ROLE_INSTRUTOR")
                .orElseThrow(() -> new ResourceNotFoundException("Role ROLE_INSTRUTOR não encontrada."));
        instrutor.getRoles().add(instrutorRole);

        Instrutor salvo = funcionarioRepository.save(instrutor);
        return toResponseDTO(salvo);
    }

    @Transactional
    public FuncionarioResponseDTO criarAdministrador(AdministradorRequestDTO dto) {
        Cargo cargo = cargoRepository.findById(dto.getCargoId())
                .orElseThrow(() -> new ResourceNotFoundException("Cargo não encontrado com ID: " + dto.getCargoId()));

        Administrador administrador = new Administrador();
        administrador.setNome(dto.getNome());
        administrador.setEmail(dto.getEmail());
        administrador.setSenha(passwordEncoder.encode(dto.getSenha()));
        administrador.setCargo(cargo);
        administrador.setSalario(dto.getSalario());

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

        funcionario.setNome(dto.getNome());
        funcionario.setEmail(dto.getEmail());

        if (dto.getCargoId() != null && !funcionario.getCargo().getId().equals(dto.getCargoId())) {
            Cargo novoCargo = cargoRepository.findById(dto.getCargoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cargo não encontrado com ID: " + dto.getCargoId()));
            funcionario.setCargo(novoCargo);
        }
        funcionario.setSalario(dto.getSalario());

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
        dto.setCargoNome(f.getCargo().getNomeCargo());
        dto.setCargoId(f.getCargo().getId());
        dto.setSalario(f.getSalario());

        // Adiciona as roles
        Set<String> rolesNames = f.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
        dto.setRoles(rolesNames); // Preenche o campo roles no DTO

        if (f instanceof Instrutor instrutor) {
            dto.setTipoFuncionario("Instrutor");
            dto.setEspecialidade(instrutor.getEspecialidade()); // Preenche a especialidade
        } else if (f instanceof Administrador administrador) {
            dto.setTipoFuncionario("Administrador");
            // dto.setDepartamentoGerenciado(administrador.getDepartamentoGerenciado()); // Se houver
        } else {
            dto.setTipoFuncionario("Desconhecido");
        }

        return dto;
    }
}
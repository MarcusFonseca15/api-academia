package sync.fit.api.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sync.fit.api.dto.request.FuncionarioRequestDTO; // Ainda pode ser usado para operações genéricas
import sync.fit.api.dto.request.InstrutorRequestDTO; // Novo DTO para Instrutor
import sync.fit.api.dto.request.AdministradorRequestDTO; // Novo DTO para Administrador
import sync.fit.api.dto.request.RecepcionistaRequestDTO; // Novo DTO para Recepcionista

import sync.fit.api.dto.response.FuncionarioResponseDTO;
import sync.fit.api.model.Funcionario;
import sync.fit.api.model.Cargo;
import sync.fit.api.model.Instrutor; // Importar Instrutor
import sync.fit.api.model.Administrador; // Importar Administrador
import sync.fit.api.model.Recepcionista; // Importar Recepcionista

import sync.fit.api.repository.CargoRepository;
import sync.fit.api.repository.FuncionarioRepository;
import sync.fit.api.exception.ResourceNotFoundException;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private CargoRepository cargoRepository;

    // Métodos de Criação Específicos
    @Transactional
    public FuncionarioResponseDTO criarInstrutor(InstrutorRequestDTO dto) {
        Cargo cargo = cargoRepository.findById(dto.getCargoId())
                .orElseThrow(() -> new ResourceNotFoundException("Cargo não encontrado com ID: " + dto.getCargoId()));

        Instrutor instrutor = new Instrutor();
        instrutor.setNome(dto.getNome());
        instrutor.setEmail(dto.getEmail());
        instrutor.setCargo(cargo);
        instrutor.setSalario(dto.getSalario());
        instrutor.setEspecialidade(dto.getEspecialidade()); // Campo específico de Instrutor

        Instrutor salvo = funcionarioRepository.save(instrutor); // Salva Instrutor
        return toResponseDTO(salvo);
    }

    @Transactional
    public FuncionarioResponseDTO criarAdministrador(AdministradorRequestDTO dto) {
        Cargo cargo = cargoRepository.findById(dto.getCargoId())
                .orElseThrow(() -> new ResourceNotFoundException("Cargo não encontrado com ID: " + dto.getCargoId()));

        Administrador administrador = new Administrador();
        administrador.setNome(dto.getNome());
        administrador.setEmail(dto.getEmail());
        administrador.setCargo(cargo);
        administrador.setSalario(dto.getSalario());
        // Defina campos específicos de Administrador aqui, se houver
        // administrador.setDepartamentoGerenciado(dto.getDepartamentoGerenciado());

        Administrador salvo = funcionarioRepository.save(administrador); // Salva Administrador
        return toResponseDTO(salvo);
    }

    @Transactional
    public FuncionarioResponseDTO criarRecepcionista(RecepcionistaRequestDTO dto) {
        Cargo cargo = cargoRepository.findById(dto.getCargoId())
                .orElseThrow(() -> new ResourceNotFoundException("Cargo não encontrado com ID: " + dto.getCargoId()));

        Recepcionista recepcionista = new Recepcionista();
        recepcionista.setNome(dto.getNome());
        recepcionista.setEmail(dto.getEmail());
        recepcionista.setCargo(cargo);
        recepcionista.setSalario(dto.getSalario());
        recepcionista.setTurnoPreferencial(dto.getTurnoPreferencial()); // Campo específico de Recepcionista

        Recepcionista salvo = funcionarioRepository.save(recepcionista); // Salva Recepcionista
        return toResponseDTO(salvo);
    }

    // --- Métodos de Leitura e Atualização (Polimórficos) ---

    @Transactional(readOnly = true)
    public List<FuncionarioResponseDTO> listarTodos() {
        // Quando você busca todos os Funcionarios, o JPA/Hibernate retorna as instâncias das subclasses corretas
        return funcionarioRepository.findAll()
                .stream()
                .map(this::toResponseDTO) // O toResponseDTO precisará lidar com os diferentes tipos
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FuncionarioResponseDTO buscarPorId(Long id) {
        Funcionario funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado com ID: " + id));
        return toResponseDTO(funcionario); // Retorna o DTO correspondente ao tipo
    }

    // O método de atualização pode se tornar mais complexo se precisar atualizar campos específicos de subclasses
    // Uma abordagem seria receber um DTO genérico e fazer um 'instanceof' ou ter métodos de atualização específicos.
    // Por simplicidade, vou manter a versão que atualiza apenas campos comuns.
    // Se precisar atualizar campos específicos de Instrutor, por exemplo, precisaria de um 'atualizarInstrutor' ou lógica mais complexa.
    @Transactional
    public FuncionarioResponseDTO atualizar(Long id, FuncionarioRequestDTO dto) {
        Funcionario funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado com ID: " + id));

        // Atualiza campos comuns
        funcionario.setNome(dto.getNome());
        funcionario.setEmail(dto.getEmail());

        // Se o ID do cargo mudou, busca e atualiza o cargo
        if (!funcionario.getCargo().getId().equals(dto.getCargoId())) {
            Cargo novoCargo = cargoRepository.findById(dto.getCargoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cargo não encontrado com ID: " + dto.getCargoId()));
            funcionario.setCargo(novoCargo);
        }
        funcionario.setSalario(dto.getSalario());

        // Lógica para campos específicos:
        // Se FuncionarioRequestDTO contiver campos específicos ou se você tiver DTOs de atualização específicos,
        // você precisaria de um 'if (funcionario instanceof Instrutor)' e fazer um cast para atualizar 'especialidade'.
        // Isso pode tornar o método 'atualizar' genérico bem complexo.
        // Uma alternativa é ter métodos de atualização específicos (ex: 'atualizarInstrutor(Long id, InstrutorRequestDTO dto)').

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

    // --- Método de Conversão para Response DTO (Polimórfico) ---
    private FuncionarioResponseDTO toResponseDTO(Funcionario f) {
        FuncionarioResponseDTO dto = new FuncionarioResponseDTO();
        dto.setId(f.getId());
        dto.setNome(f.getNome());
        dto.setEmail(f.getEmail());
        dto.setCargoNome(f.getCargo().getNomeCargo());
        dto.setCargoId(f.getCargo().getId());
        dto.setSalario(f.getSalario());

        // Agora precisamos adicionar campos específicos com base no tipo de funcionário
        if (f instanceof Instrutor instrutor) { // Usando pattern matching para instanceof (Java 16+)
            dto.setTipoFuncionario("Instrutor"); // Adicionar um campo "tipoFuncionario" ao DTO de resposta
            // Você precisaria adicionar um campo "especialidade" no FuncionarioResponseDTO
            // dto.setEspecialidade(instrutor.getEspecialidade());
        } else if (f instanceof Administrador administrador) {
            dto.setTipoFuncionario("Administrador");
            // Se Administrador tiver campos específicos, adicione-os aqui
            // dto.setDepartamentoGerenciado(administrador.getDepartamentoGerenciado());
        } else if (f instanceof Recepcionista recepcionista) {
            dto.setTipoFuncionario("Recepcionista");
            // Se Recepcionista tiver campos específicos, adicione-os aqui
            // dto.setTurnoPreferencial(recepcionista.getTurnoPreferencial());
        } else {
            dto.setTipoFuncionario("Desconhecido"); // Fallback
        }

        return dto;
    }
}
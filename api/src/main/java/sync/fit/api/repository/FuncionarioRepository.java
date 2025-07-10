package sync.fit.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sync.fit.api.model.Funcionario;

import java.util.Optional;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
    Optional<Funcionario> findByEmail(String email);
    // Adicione métodos findByDiscriminatorValue se precisar buscar por tipo,
    // mas findByEmail já resolve para autenticação.
}
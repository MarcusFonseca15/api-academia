package sync.fit.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sync.fit.api.model.Funcionario;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
}

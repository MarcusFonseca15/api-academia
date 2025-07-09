package sync.fit.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sync.fit.api.model.Exercicio;

public interface ExercicioRepository  extends JpaRepository<Exercicio, Long> {
}

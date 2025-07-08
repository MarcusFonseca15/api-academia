package sync.fit.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sync.fit.api.model.Treino;

public interface TreinoRepository extends JpaRepository<Treino, Long> {
}

package sync.fit.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sync.fit.api.model.Cargo;

public interface CargoRepository extends JpaRepository<Cargo, Long> {
}

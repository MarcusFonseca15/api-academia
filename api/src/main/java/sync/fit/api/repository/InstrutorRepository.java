package sync.fit.api.repository;

import sync.fit.api.model.Instrutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstrutorRepository extends JpaRepository<Instrutor, Long> {
    // Métodos de consulta específicos para Instrutor
}
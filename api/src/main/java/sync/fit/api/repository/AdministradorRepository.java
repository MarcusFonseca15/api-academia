package sync.fit.api.repository;

import sync.fit.api.model.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Long> {
    // Métodos de consulta específicos para Administrador, se precisar
}
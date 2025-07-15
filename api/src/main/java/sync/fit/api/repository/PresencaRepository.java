// Em sync.fit.api.repository.PresencaRepository.java
package sync.fit.api.repository;

import sync.fit.api.model.Cliente;
import sync.fit.api.model.Presenca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PresencaRepository extends JpaRepository<Presenca, Long> {
    List<Presenca> findByCliente(Cliente cliente);
    List<Presenca> findByDataHoraRegistroBetween(LocalDateTime start, LocalDateTime end);
    // Você pode adicionar métodos para contar presenças por período, etc.
}
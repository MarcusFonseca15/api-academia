package sync.fit.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sync.fit.api.model.AvaliacaoFisica;

import java.util.List;

public interface AvaliacaoFisicaRepository extends JpaRepository<AvaliacaoFisica, Long> {
    List<AvaliacaoFisica> findByClienteId(Long clienteId);
}

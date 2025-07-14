package sync.fit.api.repository;

import sync.fit.api.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import sync.fit.api.dto.response.RelatorioClientesPorPlanoDTO;
import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByEmail(String email);

    @Query("SELECT new sync.fit.api.dto.response.RelatorioClientesPorPlanoDTO(c.plano.tipo, COUNT(c)) " +
            "FROM Cliente c GROUP BY c.plano.tipo")
    List<RelatorioClientesPorPlanoDTO> contarClientesPorPlano();
}
package sync.fit.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sync.fit.api.model.Pagamento;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
}

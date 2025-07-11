package sync.fit.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sync.fit.api.model.Pagamento;
import java.util.List;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
	List<Pagamento> findByClienteId(Long clienteId);
}

package sync.fit.api.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class PagamentoResponseDTO {
    private Long id;
    private BigDecimal valor;
    private LocalDate dataPagamento;
    private String status;
    private String clienteNome;
}

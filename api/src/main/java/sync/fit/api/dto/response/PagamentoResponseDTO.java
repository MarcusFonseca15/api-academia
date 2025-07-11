package sync.fit.api.dto.response;

import lombok.Getter;
import lombok.Setter;
import sync.fit.api.model.enums.StatusPagamento;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class PagamentoResponseDTO {
    private Long id;
    private BigDecimal valor;
    private LocalDate dataPagamento;
    private LocalDate dataVencimento;
    private StatusPagamento status;
    private String clienteNome;
}

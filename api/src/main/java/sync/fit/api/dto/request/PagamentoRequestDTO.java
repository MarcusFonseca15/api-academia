package sync.fit.api.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import sync.fit.api.model.enums.StatusPagamento;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class PagamentoRequestDTO {

    @NotNull(message = "O valor do pagamento é obrigatório")
    @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero")
    private BigDecimal valor;

    @NotNull(message = "A data do pagamento é obrigatória")
    private LocalDate dataPagamento;

    @Future(message = "A data de vencimento deve ser no futuro")
    @NotNull(message = "A data de vencimento é obrigatória")
    private LocalDate dataVencimento;

    @NotNull(message = "O status é obrigatório")
    private StatusPagamento status;

    @NotNull(message = "O ID do cliente é obrigatório")
    private Long clienteId;

}

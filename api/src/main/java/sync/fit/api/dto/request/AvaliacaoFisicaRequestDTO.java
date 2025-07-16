package sync.fit.api.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AvaliacaoFisicaRequestDTO {
    @NotNull(message = "O peso é obrigatório")
    @DecimalMin(value = "0.1", message = "O peso deve ser maior que zero")
    private Double peso;

    @NotNull(message = "A altura é obrigatória")
    @DecimalMin(value = "0.1", message = "A altura deve ser maior que zero")
    private Double altura;

    @NotNull(message = "A data da avaliação é obrigatória")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataAvaliacao;

    @NotNull(message = "O ID do cliente é obrigatório")
    private Long clienteId;
}
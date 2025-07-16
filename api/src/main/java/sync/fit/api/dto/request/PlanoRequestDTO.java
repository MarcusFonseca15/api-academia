package sync.fit.api.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanoRequestDTO {
    @NotBlank(message = "O tipo do plano é obrigatório")
    private String tipo;

    @Min(value = 0, message = "O valor do plano deve ser maior ou igual a zero")
    private double valor;

    @Min(value = 1, message = "A duração do plano em meses deve ser no mínimo 1")
    private int duracaoMeses;
}
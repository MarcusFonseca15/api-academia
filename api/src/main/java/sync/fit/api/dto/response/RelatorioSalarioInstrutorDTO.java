package sync.fit.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelatorioSalarioInstrutorDTO {
    private String nome;
    private BigDecimal salario;
}

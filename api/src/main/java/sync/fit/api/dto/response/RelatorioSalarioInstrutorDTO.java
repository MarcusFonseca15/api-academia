package sync.fit.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelatorioSalarioInstrutorDTO {
    private String nome;
    private Double salario;
}

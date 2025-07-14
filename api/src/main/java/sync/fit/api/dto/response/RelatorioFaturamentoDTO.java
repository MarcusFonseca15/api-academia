package sync.fit.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelatorioFaturamentoDTO {
    private int totalClientes;
    private int planosMensais;
    private int planosSemestrais;
    private int planosAnuais;
    private double valorTotalFaturado;
}

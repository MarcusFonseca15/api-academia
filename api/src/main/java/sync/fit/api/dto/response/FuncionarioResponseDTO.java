package sync.fit.api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal; // IMPORTAR BigDecimal
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FuncionarioResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private String cargoNome;
    private Long cargoId;
    private BigDecimal salario; // <--- Mude para BigDecimal (se for esse o tipo na sua entidade)
    private String telefone;    // <--- ADICIONE ESTE CAMPO
    private String tipoFuncionario;

    private Set<String> roles;

    // Campos específicos para Instrutor
    private String especialidade;

    // Campos específicos para Administrador
    // private String departamentoGerenciado;

    // Campos específicos para Recepcionista
    private String turnoPreferencial;
}
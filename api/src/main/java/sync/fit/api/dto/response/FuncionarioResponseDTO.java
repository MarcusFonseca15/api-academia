package sync.fit.api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
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
    private BigDecimal salario;
    private String telefone;
    private String tipoFuncionario;

    private Set<String> roles;

    private String especialidade;

}
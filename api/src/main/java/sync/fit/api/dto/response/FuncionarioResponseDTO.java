package sync.fit.api.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude; // Importar
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Inclui apenas campos não nulos no JSON
public class FuncionarioResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private String cargoNome;
    private Long cargoId;
    private Double salario;
    private String tipoFuncionario; // Novo campo para indicar o tipo

    // Campos específicos para Instrutor
    private String especialidade;

    // Campos específicos para Administrador
    // private String departamentoGerenciado;

    // Campos específicos para Recepcionista
    private String turnoPreferencial;
}
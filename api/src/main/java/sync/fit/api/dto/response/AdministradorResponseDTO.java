package sync.fit.api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdministradorResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private String cargoNome;
    private Long cargoId;
    private Double salario;

    private Set<String> roles;

    // Adicione campos específicos para Administrador

}

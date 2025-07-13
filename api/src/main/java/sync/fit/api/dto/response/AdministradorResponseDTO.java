package sync.fit.api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Set; // Para as roles

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Opcional, mas útil para DTOs de resposta
public class AdministradorResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private String cargoNome;
    private Long cargoId;
    private Double salario;

    private Set<String> roles; // Roles específicas do Administrador

    // Adicione campos específicos para Administrador, se houver
    // private String departamentoGerenciado;
}

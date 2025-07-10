package sync.fit.api.dto.request;

// Exemplo de DTO base para registro de funcionário
// package sync.fit.api.dto.request;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import sync.fit.api.model.enums.TipoFuncionario; // Importar

@Getter
@Setter
public class FuncionarioRegisterRequestDTO {
    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Formato de email inválido")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    private String senha;

    @NotNull(message = "O cargo ID é obrigatório")
    private Long cargoId;

    @NotNull(message = "O salário é obrigatório")
    private Double salario;

    // Não precisamos de TipoFuncionario aqui, pois a classe específica (ex: AdministradorRegisterRequestDTO)
    // indicará o tipo.
}






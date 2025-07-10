package sync.fit.api.dto.request;

// Exemplo de DTO para registrar um Instrutor
// package sync.fit.api.dto.request;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InstrutorRegisterRequestDTO extends FuncionarioRegisterRequestDTO {
    @NotBlank(message = "A especialidade é obrigatória para o instrutor")
    private String especialidade;
}

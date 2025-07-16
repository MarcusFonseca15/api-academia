package sync.fit.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class InstrutorRegisterRequestDTO extends FuncionarioRegisterRequestDTO {
    @NotBlank(message = "A especialidade é obrigatória para o instrutor")
    private String especialidade;
}
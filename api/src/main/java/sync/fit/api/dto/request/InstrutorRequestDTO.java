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
public class InstrutorRequestDTO extends FuncionarioRequestDTO {
    @NotBlank(message = "A especialidade é obrigatória para um instrutor")
    private String especialidade;
}
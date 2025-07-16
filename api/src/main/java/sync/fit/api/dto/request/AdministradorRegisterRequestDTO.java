package sync.fit.api.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class AdministradorRegisterRequestDTO extends FuncionarioRegisterRequestDTO {
    // Campos específicos para Administrador
}
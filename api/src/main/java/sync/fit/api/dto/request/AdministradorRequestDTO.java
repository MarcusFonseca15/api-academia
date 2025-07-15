package sync.fit.api.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor; // <--- REINTRODUZIR
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor // <--- ESSENCIAL PARA O JACKSON DESERIALIZAR
@SuperBuilder
public class AdministradorRequestDTO extends FuncionarioRequestDTO {
    // Campos específicos para Administrador na atualização, se houver
}
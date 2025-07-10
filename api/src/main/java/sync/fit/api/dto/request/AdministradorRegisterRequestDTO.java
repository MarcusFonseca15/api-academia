package sync.fit.api.dto.request;
// Exemplo de DTO para registrar um Administrador
// package sync.fit.api.dto.request;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdministradorRegisterRequestDTO extends FuncionarioRegisterRequestDTO {
    // Campos específicos para Administrador, se houver
    // private String departamentoGerenciado;
}
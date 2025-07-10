package sync.fit.api.dto.request;



import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data // Gera getters, setters, toString, equals e hashCode
public class LoginRequestDTO {
    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Formato de email inválido")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    private String senha;
}
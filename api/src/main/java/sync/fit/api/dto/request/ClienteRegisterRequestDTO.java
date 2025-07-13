// sync.fit.api.dto.request.ClienteRegisterRequestDTO.java
package sync.fit.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat; // Importar para formatação de data

import java.time.LocalDate; // Importar LocalDate

@Data
public class ClienteRegisterRequestDTO {
    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Formato de email inválido")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    private String senha;

    // Novo campo: dataNascimento
    @NotNull(message = "A data de nascimento é obrigatória")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) // Garante que a data seja formatada como YYYY-MM-DD
    private LocalDate dataNascimento;

    @Pattern(regexp = "^\\(?(\\d{2})\\)?\\s?(\\d{4,5})\\-?(\\d{4})$", message = "Formato de telefone inválido. Ex: (XX) XXXX-XXXX ou (XX) XXXXX-XXXX")
    private String telefone;

    @NotNull(message = "O ID do plano é obrigatório")
    private Long planoId;

    @NotNull(message = "O ID do administrador é obrigatório")
    private Long administradorId;

    @NotNull(message = "O ID do instrutor é obrigatório")
    private Long instrutorId;
}
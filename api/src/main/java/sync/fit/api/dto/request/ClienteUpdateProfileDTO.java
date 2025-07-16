package sync.fit.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteUpdateProfileDTO {
    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Formato de email inválido")
    private String email;

    private String senha; // Opcional para atualização, se não quiser mudar a senha

    @NotNull(message = "A data de nascimento é obrigatória")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataNascimento;

    @Pattern(regexp = "^\\(?(\\d{2})\\)?\\s?(\\d{4,5})\\-?(\\d{4})$", message = "Formato de telefone inválido. Ex: (XX) XXXX-XXXX ou (XX) XXXXX-XXXX")
    private String telefone;

    // REMOVIDOS planoId e instrutorId, pois o cliente não deve alterá-los diretamente
}
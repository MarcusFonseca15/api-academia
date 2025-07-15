package sync.fit.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat; // Importar para formatação de data

import java.time.LocalDate; // Importar LocalDate

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteRequestDTO {
    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Formato de email inválido")
    private String email;

    private String senha; // Opcional na atualização

    @NotNull(message = "A data de nascimento é obrigatória") // Adicionado para atualização
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataNascimento;

    @Pattern(regexp = "^\\(?(\\d{2})\\)?\\s?(\\d{4,5})\\-?(\\d{4})$", message = "Formato de telefone inválido. Ex: (XX) XXXX-XXXX ou (XX) XXXXX-XXXX")
    private String telefone;

    // Não são mais @NotNull, permitindo remover a atribuição ou deixá-la nula.
    // Se forem sempre obrigatórios, adicione @NotNull de volta.
    private Long planoId;
    // REMOVIDO: @NotNull(message = "O ID do administrador é obrigatório")
    // REMOVIDO: private Long administradorId;
    private Long instrutorId; // Não é mais @NotNull
}
package sync.fit.api.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data; // Pode usar @Getter @Setter em vez de @Data, mas @Data é comum

@Data
public class ClienteRegisterRequestDTO { // Nome ajustado para clareza
    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Formato de email inválido")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    private String senha;

    @Pattern(regexp = "^\\(?(\\d{2})\\)?\\s?(\\d{4,5})\\-?(\\d{4})$", message = "Formato de telefone inválido. Ex: (XX) XXXX-XXXX ou (XX) XXXXX-XXXX")
    private String telefone;

    @NotNull(message = "O ID do plano é obrigatório")
    private Long planoId;

    // Se o cliente puder se auto-registrar sem um administrador específico, você pode remover este campo.
    // Se um administrador PRECISA registrar o cliente, mantenha.
    // Para um registro público, remover 'administradorId' faz mais sentido.
    // Por enquanto, vou manter como opcional para a criação pública, ou removê-lo se o cliente se registrar sozinho.
    // Vamos assumir que um administrador pode ser associado no momento do registro.
    @NotNull(message = "O ID do administrador é obrigatório")
    private Long administradorId;
}
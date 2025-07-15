package sync.fit.api.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate; // Adicionado import

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private LocalDate dataNascimento; // Adicionado data de nascimento ao response
    private String planoTipo;
    // REMOVIDO: private String administradorNome;
    private String instrutorNome;
}
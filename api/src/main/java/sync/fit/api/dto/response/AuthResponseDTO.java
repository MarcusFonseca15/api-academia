package sync.fit.api.dto.response;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor; // É bom ter para desserialização, caso você o use em alguma requisição (embora seja mais comum em respostas)

@Data // Gera getters, setters, toString, equals e hashCode
@AllArgsConstructor // Gera um construtor com todos os campos
@NoArgsConstructor // Gera um construtor sem argumentos (necessário para Jackson e outras libs)
public class AuthResponseDTO {
    private String token; // O JWT gerado
    private String tipo;  // O tipo/role do usuário (ex: "ADMIN", "INSTRUTOR", "CLIENTE", "RECEPCIONISTA")
    private String email; // O email do usuário que fez login/foi registrado
}
// sync.fit.api.dto.response.ClienteResponseDTO.java
package sync.fit.api.dto.response;

import lombok.Data; // Adicionei @Data para simplificar getters/setters/construtores padrão
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data // Use @Data para gerar getters, setters, toString, equals, hashCode e construtores
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String planoTipo;
    private String administradorNome;
    private String instrutorNome; // NOVO CAMPO
}
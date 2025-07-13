package sync.fit.api.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Set; // Se quiser incluir roles específicas do Instrutor aqui

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InstrutorResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private String cargoNome;
    private Long cargoId;
    private Double salario;
    private String especialidade; // Campo específico de instrutor

    private Set<String> roles; // Opcional: Se quiser que o DTO de Instrutor também tenha as roles
}
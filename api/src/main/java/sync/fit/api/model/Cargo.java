package sync.fit.api.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cargo") // Mapeia para a tabela 'cargo' no banco de dados
@Data // Gera getters, setters, toString, equals e hashCode
@NoArgsConstructor // Construtor sem argumentos
@AllArgsConstructor // Construtor com todos os argumentos
public class Cargo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Geração automática de ID
    private Long id;

    // Adicione esta linha para mapear explicitamente nomeCargo para nome_cargo
    @Column(name = "nome_cargo", nullable = false, unique = true)
    private String nomeCargo;

    // Se precisar, adicione outros atributos para o Cargo aqui (ex: descricao, nivel)

}
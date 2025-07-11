package sync.fit.api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "role") // Nome da tabela no banco de dados
@Data // Gera getters, setters, toString, equals e hashCode
@NoArgsConstructor // Construtor sem argumentos
@AllArgsConstructor // Construtor com todos os argumentos
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // Ex: "ROLE_ADMIN", "ROLE_CLIENTE"

    // Construtor adicional se preferir
    public Role(String name) {
        this.name = name;
    }
}
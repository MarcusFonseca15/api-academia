package sync.fit.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import lombok.Data; // Ou @Getter, @Setter
import lombok.EqualsAndHashCode; // Para incluir campos da superclasse no equals/hashCode

@Entity
// Este valor será armazenado na coluna 'tipo_funcionario' da tabela 'funcionario'
@DiscriminatorValue("Recepcionista")
@Data // Inclui getters, setters, toString, equals e hashCode
@NoArgsConstructor // Construtor sem argumentos para JPA
@EqualsAndHashCode(callSuper = true) // Importante: Garante que equals/hashCode considerem campos da superclasse
public class Recepcionista extends Funcionario {

    // Campo específico para Recepcionista
    @Column(nullable = true) // Pode ser nulo, pois nem toda Recepcionista precisa de um turno preferencial
    private String turnoPreferencial; // Ex: "Manhã", "Tarde", "Noite"

    // Construtor completo chamando o construtor da superclasse.
    // O Lombok @AllArgsConstructor na superclasse Funcionario, se usado,
    // e aqui no Recepcionista com callSuper = true, geraria um construtor semelhante.
    public Recepcionista(Long id, String nome, String email, Cargo cargo, Double salario, String turnoPreferencial) {
        super(id, nome, email, cargo, salario);
        this.turnoPreferencial = turnoPreferencial;
    }
}
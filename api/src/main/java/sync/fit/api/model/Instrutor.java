package sync.fit.api.model;


import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode; // Para incluir campos da superclasse no equals/hashCode

@Entity
@DiscriminatorValue("Instrutor") // Valor que será armazenado em 'tipo_funcionario'
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true) // Importante: Garante que equals/hashCode considerem campos da superclasse
public class Instrutor extends Funcionario {

  // Exemplo de campo específico de Instrutor
    private String especialidade;

    // Construtor sem argumentos necessário para JPA
    // Construtor completo para Instrutor, chamando o construtor da superclasse
    public Instrutor(Long id, String nome, String email, Cargo cargo, Double salario, String especialidade) {
        super(id, nome, email, cargo, salario);
        this.especialidade = especialidade;
    }
}
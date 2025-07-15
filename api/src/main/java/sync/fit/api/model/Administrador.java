
// sync.fit.api.model.Administrador.java
package sync.fit.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "administrador")
@PrimaryKeyJoinColumn(name = "id") // Mapeia o ID para a tabela pai (funcionario)
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Administrador extends Funcionario {

    // Construtor para Administrador
    public Administrador(String nome, String email, String senha, String telefone, BigDecimal salario, Cargo cargo) {
        super(nome, email, senha, telefone, salario, cargo);
    }
    // Pode ter campos específicos de Administrador aqui, se necessário
    // private String departamentoGerenciado;
}

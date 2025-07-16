

package sync.fit.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "instrutor")
@PrimaryKeyJoinColumn(name = "id") // Mapeia o ID para a tabela pai (funcionario)
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Instrutor extends Funcionario {

    @Column(nullable = false)
    private String especialidade;

    @OneToMany(mappedBy = "instrutor")
    private List<Cliente> alunos = new ArrayList<>();


    public Instrutor(String nome, String email, String senha, String telefone, BigDecimal salario, Cargo cargo, String especialidade) {
        super(nome, email, senha, telefone, salario, cargo);
        this.especialidade = especialidade;
    }
}
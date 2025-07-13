package sync.fit.api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "instrutor")
@PrimaryKeyJoinColumn(name = "id") // <--- CORRIGIDO AQUI!
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = "alunos")
public class Instrutor extends Funcionario {

    private String especialidade;

    @OneToMany(mappedBy = "instrutor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Cliente> alunos = new ArrayList<>();

    public Instrutor(String nome, String email, String senha, Cargo cargo, Double salario, String especialidade) {
        super(nome, email, senha, cargo, salario);
        this.especialidade = especialidade;
    }
}
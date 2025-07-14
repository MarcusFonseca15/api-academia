package sync.fit.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

// Remova os imports não utilizados se o Lombok AllArgsConstructor for gerado no construtor padrão
// import java.util.HashSet;
// import java.util.List;
// import java.util.Set;

@Entity
@Table(name = "administrador")
@PrimaryKeyJoinColumn(name = "id") // <--- CORRIGIDO AQUI!
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Administrador extends Funcionario {

    // Se Administrador tiver atributos próprios, eles iriam aqui. Ex:
    // private String departamentoGerenciado;

    // Construtor para conveniência (sem ID e roles)
    public Administrador(String nome, String email, String senha, Cargo cargo, Double salario) {
        super(nome, email, senha, cargo, salario);
        // Ao criar um Administrador, você deve adicionar a role 'ADMIN' aqui ou no serviço
        // Role roleAdmin = new Role("ROLE_ADMIN"); // Supondo que você tem uma Role
        // this.getRoles().add(roleAdmin); // Isso deve ser feito via serviço ou persistência inicial
    }
}
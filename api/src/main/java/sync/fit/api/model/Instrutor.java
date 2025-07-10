package sync.fit.api.model;


import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Entity
@DiscriminatorValue("INSTRUTOR") // Use o nome do enum aqui
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Instrutor extends Funcionario {

    private String especialidade;

    public Instrutor(String nome, String email, String senha, Cargo cargo, Double salario, String especialidade) {
        super(nome, email, senha, cargo, salario);
        this.especialidade = especialidade;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_INSTRUTOR")); // Role para instrutores
    }
}
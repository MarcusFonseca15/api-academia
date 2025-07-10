package sync.fit.api.model;


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
@DiscriminatorValue("ADMINISTRADOR") // Use o nome do enum aqui para consistência
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Administrador extends Funcionario {

    // Construtor sem argumentos necessário para JPA
    // Construtor completo para Administrador
    public Administrador(String nome, String email, String senha, Cargo cargo, Double salario) {
        super(nome, email, senha, cargo, salario);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_ADMIN")); // Role para administradores
    }
}
package sync.fit.api.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails; // Importar

import java.util.Collection; // Importar

@Entity
@Table(name = "funcionario")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_funcionario", discriminatorType = DiscriminatorType.STRING)
@Data
@NoArgsConstructor
public abstract class Funcionario implements UserDetails { // Agora implementa UserDetails

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false) // Adicione o campo senha aqui
    private String senha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cargo_id", nullable = false)
    private Cargo cargo; // Considere se Cargo é realmente necessário se TipoFuncionario já define a role

    @Column(nullable = false)
    private Double salario;

    // Construtor para ser usado pelas subclasses
    public Funcionario(String nome, String email, String senha, Cargo cargo, Double salario) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.cargo = cargo;
        this.salario = salario;
    }

    // --- Implementação dos métodos de UserDetails ---
    // ATENÇÃO: getAuthorities() será implementado em CADA SUBCLASSE
    // para retornar a ROLE específica de cada uma.
    @Override
    public abstract Collection<? extends GrantedAuthority> getAuthorities(); // Abstrato aqui

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email; // O email será o nome de usuário para login
    }

    // Mantidos como true para simplicidade, ajuste se precisar de lógica de bloqueio/expiração
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
package sync.fit.api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor; // Removendo @AllArgsConstructor para gerenciar construtores manualmente
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "cliente")
@Data
@NoArgsConstructor // Mantém o construtor sem argumentos para JPA
@EqualsAndHashCode(of = "id")
// @AllArgsConstructor // <--- Remova ou comente esta anotação se você quiser gerenciar os construtores manualmente
public class Cliente implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false)
    private LocalDate dataNascimento;

    @Column(nullable = false) // <--- Novo campo para Telefone, assumindo que é obrigatório
    private String telefone; // <--- Adicionado o campo telefone

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plano_id", nullable = true)
    private Plano plano;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "administrador_id", nullable = true)
    private Administrador administrador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instrutor_id", nullable = true)
    private Instrutor instrutor;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "cliente_roles",
            joinColumns = @JoinColumn(name = "cliente_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    // Construtor para conveniência que inclui TODOS os campos necessários para o registro
    // Mantenha esta ordem de argumentos e certifique-se de que corresponda à chamada no AuthService
    public Cliente(String nome, String email, String senha, LocalDate dataNascimento, String telefone, Plano plano, Administrador administrador, Instrutor instrutor) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone; // <--- Atribuindo o telefone
        this.plano = plano;
        this.administrador = administrador;
        this.instrutor = instrutor;
    }

    // --- Métodos UserDetails (permanecem inalterados) ---
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

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
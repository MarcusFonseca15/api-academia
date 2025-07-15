package sync.fit.api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
// import lombok.AllArgsConstructor; // Removendo @AllArgsConstructor para gerenciar construtores manualmente
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "cliente")
@Data
@NoArgsConstructor // Mantém o construtor sem argumentos para JPA
@EqualsAndHashCode(of = "id")
// @AllArgsConstructor // <--- Remova ou comente esta anotação se você quiser gerenciar os construtores manualmente
public class Cliente implements UserDetails, UserIdentifiable {

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

    @Column(nullable = false)
    private String telefone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plano_id", nullable = true)
    private Plano plano;

    // REMOVIDO: Relacionamento com Administrador
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "administrador_id", nullable = true)
    // private Administrador administrador;


    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Presenca> presencas = new ArrayList<>();

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

    // Construtor para conveniência - ATUALIZADO: REMOVIDO 'Administrador administrador'
    public Cliente(String nome, String email, String senha, LocalDate dataNascimento, String telefone, Plano plano, Instrutor instrutor) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
        this.plano = plano;
        // this.administrador = administrador; // Removido
        this.instrutor = instrutor;
    }

    // --- Métodos UserDetails e UserIdentifiable (permanecem inalterados) ---
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

    @Override
    public Long getId() {
        return this.id;
    }
}
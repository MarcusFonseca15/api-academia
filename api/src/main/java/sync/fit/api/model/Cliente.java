package sync.fit.api.model;



import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor; // Remover este se você quiser construtor específico
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails; // Importar

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "cliente")
@Data
@NoArgsConstructor
@AllArgsConstructor // Remover este para ter o construtor customizado abaixo
public class Cliente implements UserDetails { // Implementa UserDetails

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false) // Adicione o campo senha para o cliente
    private String senha;

    @Column(length = 20)
    private String telefone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plano_id")
    private Plano plano;

    // Se o administrador for um Funcionario do tipo Administrador
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "administrador_id")
    private Administrador administrador; // Agora especificando Administrador

    // Relacionamentos omitidos para brevidade, mas devem permanecer

    // Construtor customizado para facilitar a criação (remova @AllArgsConstructor se usar este)
    public Cliente(String nome, String email, String senha, String telefone, Plano plano, Administrador administrador) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
        this.plano = plano;
        this.administrador = administrador;
    }

    // --- Métodos de UserDetails ---
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_CLIENTE")); // Role para clientes
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email; // O email será o nome de usuário para login
    }

    // Mantidos como true para simplicidade
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
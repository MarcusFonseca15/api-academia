package sync.fit.api.model;

import jakarta.persistence.*;

@Entity
public class Instrutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String especialidade;

    @ManyToOne
    @JoinColumn(name = "administrador_id")
    private Administrador administrador;

    public Instrutor(Long id, String nome, String especialidade, Administrador administrador) {
        this.id = id;
        this.nome = nome;
        this.especialidade = especialidade;
        this.administrador = administrador;
    }

    public Instrutor() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public Administrador getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Administrador administrador) {
        this.administrador = administrador;
    }
}

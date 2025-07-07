package sync.fit.api.model;

import jakarta.persistence.*;

@Entity
public class Exercicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Lob
    private String descricao;

    private Integer repeticoes;

    private Integer series;

    @ManyToOne
    @JoinColumn(name = "treino_id")
    private Treino treino;

    public Exercicio(Long id, String nome, String descricao, Integer repeticoes, Integer series, Treino treino) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.repeticoes = repeticoes;
        this.series = series;
        this.treino = treino;
    }

    public Exercicio() {

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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getRepeticoes() {
        return repeticoes;
    }

    public void setRepeticoes(Integer repeticoes) {
        this.repeticoes = repeticoes;
    }

    public Integer getSeries() {
        return series;
    }

    public void setSeries(Integer series) {
        this.series = series;
    }

    public Treino getTreino() {
        return treino;
    }

    public void setTreino(Treino treino) {
        this.treino = treino;
    }
}

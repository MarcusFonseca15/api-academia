package sync.fit.api.dto.response;

import java.util.List;

public class TreinoResponseDTO {
    private Long id;
    private String descricao;
    private String nomeCliente;
    private List<ExercicioSimplesDTO> exercicios;

    public TreinoResponseDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public List<ExercicioSimplesDTO> getExercicios() {
        return exercicios;
    }

    public void setExercicios(List<ExercicioSimplesDTO> exercicios) {
        this.exercicios = exercicios;
    }
}

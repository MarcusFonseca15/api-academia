package sync.fit.api.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class FuncionarioRequestDTO {

    private String nome;
    private String email;


    @NotNull(message = "O ID do cargo é obrigatório")
    @PositiveOrZero(message = "O ID do cargo deve ser um número positivo")
    private Long cargoId; // <-- Alterado: Agora recebe o ID do Cargo

    private Double salario;


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getCargoId() {
        return cargoId;
    }

    public void setCargoId(Long cargoId) {
        this.cargoId = cargoId;
    }

    public Double getSalario() {
        return salario;
    }

    public void setSalario(Double salario) {
        this.salario = salario;
    }
}

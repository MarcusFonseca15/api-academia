package sync.fit.api.dto.request;
// sync.fit.api.dto.request.RecepcionistaRequestDTO.java


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class RecepcionistaRequestDTO {
    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    private Double salario; // <--- ADICIONE ESTA LINHA**

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    private String senha;

    private String telefone;

    @NotBlank(message = "O turno preferencial é obrigatório para uma recepcionista")
    private String turnoPreferencial; // Campo específico de Recepcionista



    @NotNull(message = "O ID do cargo é obrigatório")
    @PositiveOrZero(message = "O ID do cargo deve ser um número positivo")
    private Long cargoId; // Ou defina o ID fixo do Cargo "Recepcionista" no serviço

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getTurnoPreferencial() { return turnoPreferencial; }
    public void setTurnoPreferencial(String turnoPreferencial) { this.turnoPreferencial = turnoPreferencial; }
    public Double getSalario() { return salario; }
    public void setSalario(Double salario) { this.salario = salario; }
    public Long getCargoId() { return cargoId; }
    public void setCargoId(Long cargoId) { this.cargoId = cargoId; }


}
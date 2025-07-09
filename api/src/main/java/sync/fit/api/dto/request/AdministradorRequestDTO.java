package sync.fit.api.dto.request;
// sync.fit.api.dto.request.AdministradorRequestDTO.java

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class AdministradorRequestDTO {
    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    private String senha; // Lembre-se de criptografar no serviço!

    private Double salario; // <--- ADICIONE ESTA LINHA**

    private String telefone; // Telefone pode ser opcional

    // Salário (se administradores tiverem salário e for enviado separadamente)
    // private Double salario; // Se salário não for um atributo na entidade Administrador, remova

    // Cargo ID não é estritamente necessário aqui, já que o tipo é implícito.
    // Mas se você quiser validar que é um ID de cargo de Administrador, pode manter.
    // Ou pode simplesmente definir o cargo_id fixo como o ID do Cargo "Administrador" no serviço.
    @NotNull(message = "O ID do cargo é obrigatório")
    @PositiveOrZero(message = "O ID do cargo deve ser um número positivo")
    private Long cargoId;

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public Long getCargoId() { return cargoId; }
    public void setCargoId(Long cargoId) { this.cargoId = cargoId; }

    public Double getSalario() {
        return salario;
    }

    public void setSalario(Double salario) {
        this.salario = salario;
    }
}
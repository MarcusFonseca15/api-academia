package sync.fit.api.dto.response;

public class ClienteResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String planoTipo;
    private String administradorNome;

    public ClienteResponseDTO() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getPlanoTipo() {
        return planoTipo;
    }

    public void setPlanoTipo(String planoTipo) {
        this.planoTipo = planoTipo;
    }

    public String getAdministradorNome() {
        return administradorNome;
    }

    public void setAdministradorNome(String administradorNome) {
        this.administradorNome = administradorNome;
    }
}

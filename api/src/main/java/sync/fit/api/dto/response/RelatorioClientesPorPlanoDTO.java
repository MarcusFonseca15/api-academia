package sync.fit.api.dto.response;

public class RelatorioClientesPorPlanoDTO {

    private String nomePlano;
    private Long quantidadeClientes;

    public RelatorioClientesPorPlanoDTO(String nomePlano, Long quantidadeClientes) {
        this.nomePlano = nomePlano;
        this.quantidadeClientes = quantidadeClientes;
    }

    public String getNomePlano() {
        return nomePlano;
    }

    public void setNomePlano(String nomePlano) {
        this.nomePlano = nomePlano;
    }

    public Long getQuantidadeClientes() {
        return quantidadeClientes;
    }

    public void setQuantidadeClientes(Long quantidadeClientes) {
        this.quantidadeClientes = quantidadeClientes;
    }
}
